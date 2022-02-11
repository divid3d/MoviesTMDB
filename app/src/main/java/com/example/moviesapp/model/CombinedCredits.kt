package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CombinedCredits(
    val cast: List<CombinedCreditsCast>,

    val crew: List<CombinedCreditsCrew>
)

@JsonClass(generateAdapter = true)
data class CombinedCreditsCast(
    val id: Int,

    val name: String?,

    val character: String,

    val title: String?,

    @Json(name = "media_type")
    val mediaType: MediaType,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Transient
    val posterUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class CombinedCreditsCrew(
    val id: Int,

    val department: String,

    val job: String,

    val title: String?,

    @Json(name = "media_type")
    val mediaType: MediaType,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Transient
    val posterUrl: String? = null
)