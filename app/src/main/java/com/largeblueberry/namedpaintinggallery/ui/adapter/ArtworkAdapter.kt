package com.largeblueberry.namedpaintinggallery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.largeblueberry.namedpaintinggallery.data.models.Artwork
import com.largeblueberry.namedpaintinggallery.databinding.ArtitemBinding

class ArtworkAdapter(
    private val artworks: List<Artwork>,
    private val onLoadImage: (String, ImageView) -> Unit
) : RecyclerView.Adapter<ArtworkAdapter.ArtworkViewHolder>() {

    inner class ArtworkViewHolder(private val binding: ArtitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artwork: Artwork) {
            binding.artTitle.text = artwork.title
            binding.artistName.text = artwork.artistName
            onLoadImage(artwork.imageUrl, binding.artImage) // 이미지 로드

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val binding = ArtitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bind(artworks[position])
    }

    override fun getItemCount(): Int = artworks.size
}
