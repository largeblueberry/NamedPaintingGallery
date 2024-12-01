package com.largeblueberry.namedpaintinggallery.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.largeblueberry.namedpaintinggallery.R
import com.largeblueberry.namedpaintinggallery.data.firebase.FirebaseHelper
import com.largeblueberry.namedpaintinggallery.data.models.Artwork
import com.largeblueberry.namedpaintinggallery.databinding.ActivityMainBinding
import com.largeblueberry.namedpaintinggallery.ui.adapter.ArtworkAdapter
import com.largeblueberry.namedpaintinggallery.ui.fragment.login.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var artworkAdapter: ArtworkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewPager2 초기화
        setupViewPager()

        // Firestore에서 데이터 로드
        loadArtworks()
    }

    // ViewPager2 설정
    private fun setupViewPager() {
        artworkAdapter = ArtworkAdapter(emptyList()) { imageUrl, imageView ->
            // Glide를 사용하여 이미지 로드
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // 로딩 중 기본 이미지
                .error(R.drawable.error) // 실패 시 기본 이미지
                .into(imageView)

            // 이미지 색상 추출 및 배경화면 설정
            loadDynamicBackground(imageUrl)
        }
        binding.viewPager.adapter = artworkAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 수평 넘기기
    }

    // Firestore에서 데이터를 가져오는 함수
    private fun loadArtworks() {
        FirebaseHelper.fetchArtworksFromFirestore(
            onSuccess = { artworks ->
                val artworkList = artworks.map { data ->
                    Artwork(
                        title = data["title"] as String,
                        artistName = data["artistName"] as String,
                        imageUrl = data["imageUrl"] as String
                    )//이미지 url은 토큰 눌러 복사로 해결
                }
                updateViewPager(artworkList)// 일단 2개 옆으로 넘겨서 바인딩하는 거 성공
            },
            onFailure = { exception ->
                Toast.makeText(this, "데이터를 불러오지 못했습니다: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error loading artworks", exception)
            }
        )
    }

    // ViewPager2의 데이터를 업데이트
    private fun updateViewPager(artworks: List<Artwork>) {
        artworkAdapter = ArtworkAdapter(artworks) { imageUrl, imageView ->
            // Glide를 사용하여 이미지 로드
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView)

            // 이미지 색상 추출 및 배경화면 설정
            loadDynamicBackground(imageUrl)
        }
        binding.viewPager.adapter = artworkAdapter
    }

    // 이미지 색상을 기반으로 배경화면 설정
    private fun loadDynamicBackground(imageUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate { palette ->
                        if (palette == null) {
                            Log.e("MainActivity", "Palette generation failed.")//에러 로그 찍기
                            binding.root.setBackgroundColor(0xFFFFFF) // 흰색 배경 뜨면 ㅈ땐거
                        } else {
                            val vibrantColor = palette.getVibrantColor(0xFFFFFF)
                            val mutedColor = palette.getMutedColor(0xEEEEEE)
                            Log.d("MainActivity", "Extracted Colors: Vibrant=$vibrantColor, Muted=$mutedColor")

                            val backgroundBitmap = createDynamicBackground(listOf(vibrantColor, mutedColor))

                            // Window 전체에 배경 설정
                            window.decorView.setBackgroundDrawable(BitmapDrawable(resources, backgroundBitmap))
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.root.setBackgroundColor(0xFFFFFF)
                }
            })
    }

    // 동적 배경화면 생성
    private fun createDynamicBackground(colors: List<Int>): Bitmap {
        val width = 1080 // 배경화면 너비 그냥 임시로 정함. 나중에 수정
        val height = 1920 // 높이 이것도 임시로 정함.
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint()

        // 각 색상별로 배경 분할 -> 색상 추출하는 건데 이거 분할할 필요가 없을 거 같은데, 아 그냥 두자.
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
                    Toast.makeText(this, "현재 화면입니다.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_login -> {
                    // SettingsActivity 대신 SettingsFragment로 전환
                    openSettingsFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun openSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment()) // fragment_container는 FrameLayout의 ID
            .addToBackStack(null) // 뒤로 가기 지원
            .commit()
    }
}
