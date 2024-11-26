package com.largeblueberry.namedpaintinggallery.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.largeblueberry.namedpaintinggallery.R


object ImageLoader {
    fun loadImageFromFirebase(imageId: String, imageView: ImageView) {
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(imageView.context)
                .load(uri)
                .placeholder(R.drawable.placeholder) // 로딩 중일 때 보여줄 기본 이미지
                .into(imageView)
        }.addOnFailureListener {
            imageView.setImageResource(R.drawable.error) // 실패 시 기본 이미지 표시
        }
    }
}
