package com.largeblueberry.namedpaintinggallery.data.firebase

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseHelper {
    // Firebase Storage에 이미지 업로드

    // Firestore에서 데이터 가져오기
    fun fetchArtworksFromFirestore(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (Exception) -> Unit) {
        FirebaseFirestore.getInstance().collection("artworks")
            .get()
            .addOnSuccessListener { result ->
                val artworks = result.documents.mapNotNull { it.data }
                onSuccess(artworks)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
