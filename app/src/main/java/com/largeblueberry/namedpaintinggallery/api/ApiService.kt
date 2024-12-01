package com.largeblueberry.namedpaintinggallery.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ApiService {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: OpenAIApi = retrofit.create(OpenAIApi::class.java)

    fun fetchArtworkInfo(
        inputText: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val request = GPTRequest(
            messages = listOf(
                mapOf("role" to "user", "content" to inputText)
            )
        )

        api.getArtworkInfo(request).enqueue(object : Callback<GPTResponse> {
            override fun onResponse(call: Call<GPTResponse>, response: Response<GPTResponse>) {
                if (response.isSuccessful) {
                    val content = response.body()?.choices?.get(0)?.message?.get("content")
                    if (content != null) {
                        onSuccess(content)
                    } else {
                        onError(Exception("Empty response"))
                    }
                } else {
                    onError(Exception("Response error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GPTResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
}