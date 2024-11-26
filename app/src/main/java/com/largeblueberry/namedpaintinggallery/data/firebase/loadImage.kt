package com.largeblueberry.namedpaintinggallery.data.firebase

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.largeblueberry.namedpaintinggallery.R

object ImageLoader {//이 함수는 자주 쓰일 거 같아서 일단 따로 빼고 함. 메인 엑티비티가 너무 길어짐.
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