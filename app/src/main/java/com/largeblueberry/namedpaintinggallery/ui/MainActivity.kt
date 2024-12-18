package com.largeblueberry.namedpaintinggallery.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.largeblueberry.namedpaintinggallery.R
import com.largeblueberry.namedpaintinggallery.api.ApiService
import com.largeblueberry.namedpaintinggallery.data.firebase.FirebaseHelper
import com.largeblueberry.namedpaintinggallery.data.models.Artwork
import com.largeblueberry.namedpaintinggallery.databinding.ActivityMainBinding
import com.largeblueberry.namedpaintinggallery.ui.adapter.ArtworkAdapter
import com.largeblueberry.namedpaintinggallery.ui.fragment.login.SettingsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var artworkAdapter: ArtworkAdapter
    private lateinit var tts: TextToSpeech
    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TTS 초기화
        tts = TextToSpeech(this, this)

        // ViewPager2 초기화
        setupViewPager()

        // Firestore에서 데이터 로드
        loadArtworks()

        // 하단바 설정
        setupBottomNavigation()
        // 하트 버튼 클릭 이벤트
        setupHeartButton()
    }
    // TTS 초기화 콜백
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.KOREAN // 한국어 설정
        } else {
            Toast.makeText(this, "TTS 초기화 실패", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupHeartButton() {
        val heartButton: ImageButton = binding.sendOrHeartButton
        heartButton.setOnClickListener {
            val currentArtwork = getCurrentArtwork() // 현재 ViewPager에서 보이는 Artwork 정보 가져오기
            currentArtwork?.let { artwork ->
                fetchArtworkExplanation(artwork)
            } ?: Toast.makeText(this, "현재 명화 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    // 현재 ViewPager에서 보이는 Artwork 가져오기
    private fun getCurrentArtwork(): Artwork? {
        val currentPosition = binding.viewPager.currentItem
        return if (::artworkAdapter.isInitialized && currentPosition < artworkAdapter.itemCount) {
            artworkAdapter.getArtworkAt(currentPosition)
        } else {
            null
        }
    }

    // GPT API 호출 및 TTS 처리
    private fun fetchArtworkExplanation(artwork: Artwork) {
        val prompt = "명화 '${artwork.title}'에 대한 간단한 설명을 알려주세요."
        apiService.fetchArtworkInfo(prompt,
            onSuccess = { explanation ->
                speakText(explanation) // TTS로 음성 출력
            },
            onError = { error ->
                Toast.makeText(this, "GPT API 호출 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "GPT API 호출 실패", error)
            }
        )
    }

    // TTS로 텍스트 읽기
    private fun speakText(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        // TTS 자원 해제
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }

    // ViewPager2 설정
    private fun setupViewPager() {
        artworkAdapter = ArtworkAdapter(emptyList()) { imageUrl, imageView ->
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            lifecycleScope.launch {
                loadDynamicBackground(imageUrl) // 비동기로 배경 설정
            }
        }
        binding.viewPager.adapter = artworkAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    // Firestore에서 데이터를 가져오는 함수
    private fun loadArtworks() {
        FirebaseHelper.fetchArtworksFromFirestore(
            onSuccess = { artworks -> // 성공적으로 데이터를 가져온 경우
                val artworkList = artworks.map { data ->
                    Artwork(
                        title = data["title"] as String,
                        artistName = data["artistName"] as String,
                        imageUrl = data["imageUrl"] as String
                    )
                }
                updateViewPager(artworkList) // 데이터를 ViewPager로 업데이트
            },
            onFailure = { exception -> // 데이터 로드에 실패한 경우
                Toast.makeText(this, "데이터를 불러오지 못했습니다: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error loading artworks", exception)
            }
        )
    }


    // ViewPager2의 데이터를 업데이트
    private fun updateViewPager(artworks: List<Artwork>) {
        artworkAdapter = ArtworkAdapter(artworks) { imageUrl, imageView ->
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            lifecycleScope.launch {
                loadDynamicBackground(imageUrl) // 비동기로 배경 설정
            }
        }
        binding.viewPager.adapter = artworkAdapter
    }

    // 이미지 색상을 기반으로 배경화면 설정
    private suspend fun loadDynamicBackground(imageUrl: String) {
        withContext(Dispatchers.IO) {
            Glide.with(this@MainActivity)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        Palette.from(resource).generate { palette ->
                            if (palette == null) {
                                Log.e("MainActivity", "Palette generation failed.")
                                lifecycleScope.launch {
                                    binding.root.setBackgroundColor(0xFFFFFF)
                                }
                            } else {
                                val vibrantColor = palette.getVibrantColor(0xFFFFFF)
                                val mutedColor = palette.getMutedColor(0xEEEEEE)
                                Log.d("MainActivity", "Extracted Colors: Vibrant=$vibrantColor, Muted=$mutedColor")

                                val backgroundBitmap = createDynamicBackground(listOf(vibrantColor, mutedColor))

                                lifecycleScope.launch {
                                    // Window 전체에 배경 설정
                                    window.decorView.setBackgroundDrawable(BitmapDrawable(resources, backgroundBitmap))
                                }
                            }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        lifecycleScope.launch {
                            binding.root.setBackgroundColor(0xFFFFFF)
                        }
                    }
                })
        }
    }

    // 동적 배경화면 생성
    private fun createDynamicBackground(colors: List<Int>): Bitmap {
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint()

        for (i in colors.indices) {
            paint.color = colors[i]
            canvas.drawRect(
                0f,
                (height / colors.size * i).toFloat(),
                width.toFloat(),
                (height / colors.size * (i + 1)).toFloat(),
                paint
            )
        }
        return bitmap
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    openMainActivity() // 메인 액티비티로 이동
                    true
                }
                R.id.action_login -> {
                    openSettingsFragment()
                    true
                }
                else -> false
            }
        }
    }
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }

    private fun openSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }
}