package com.example.moviesapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesResponse(
    val id: Int,
    val backdrops: List<Backdrop>
)