package com.largeblueberry.namedpaintinggallery.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.largeblueberry.namedpaintinggallery.R
import com.largeblueberry.namedpaintinggallery.data.firebase.FirebaseHelper
import com.largeblueberry.namedpaintinggallery.data.models.Artwork
import com.largeblueberry.namedpaintinggallery.databinding.ActivityMainBinding
import com.largeblueberry.namedpaintinggallery.ui.adapter.ArtworkAdapter

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
            Log.d("MainActivity", "Loading image from URL: $imageUrl")
        }
        binding.viewPager.adapter = artworkAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 수평 스와이프
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
                    )
                }
                updateViewPager(artworkList)
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
        }
        binding.viewPager.adapter = artworkAdapter
    }
}
