package com.largeblueberry.namedpaintinggallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.largeblueberry.namedpaintinggallery.data.models.Artwork
import com.largeblueberry.namedpaintinggallery.databinding.ArtitemBinding

class ArtworkAdapter(
    private var artworks: List<Artwork>,
    private val onLoadImage: (String, ImageView) -> Unit
) : RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder>() {

    // ViewHolder 정의
    inner class ArtworkViewHolder(private val binding: ArtitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artwork: Artwork) {
            binding.artTitle.text = artwork.title
            binding.artistName.text = artwork.artistName
            onLoadImage(artwork.imageUrl, binding.artImage) // 이미지 로드 콜백
        }
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val binding = ArtitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtworkViewHolder(binding)
    }

    // ViewHolder와 데이터 연결
    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bind(artworks[position])
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = artworks.size

    // 현재 명화 정보를 가져오는 메서드 추가
    fun getArtworkAt(position: Int): Artwork? {
        return if (position in artworks.indices) artworks[position] else null
    }

    // 데이터 업데이트 메서드
    fun updateArtworks(newArtworks: List<Artwork>) {
        artworks = newArtworks
        notifyDataSetChanged() // RecyclerView 갱신
    }
}