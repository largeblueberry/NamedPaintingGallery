package com.largeblueberry.namedpaintinggallery.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers("Authorization: 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",
        "Content-Type: application/json")
    @POST("v1/chat/completions")
    fun getArtworkInfo(@Body request: GPTRequest): Call<GPTResponse>
}