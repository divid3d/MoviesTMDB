package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Network(
    val id: Int,

    val name: String,

    @Json(name = "logo_path")
    val logoPath: String?,

    @Json(name = "origin_country")
    val originCountry: String,

    @Transient
    val logoUrl: String? = null
)