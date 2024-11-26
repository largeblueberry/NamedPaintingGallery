package com.largeblueberry.namedpaintinggallery.data.firebase

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseHelper {
    // Firebase Storage에 이미지 업로드
    fun uploadImageToStorage(filePath: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference.child("artworks/${filePath.lastPathSegment}")
        storageReference.putFile(filePath)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString()) // 업로드된 파일의 다운로드 URL 반환
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Firestore에 데이터 저장
    fun saveArtworkToFirestore(title: String, artistName: String, imageUrl: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val artwork = hashMapOf(
            "title" to title,
            "artistName" to artistName,
            "imageUrl" to imageUrl
        )
        FirebaseFirestore.getInstance().collection("artworks")
            .add(artwork)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

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
