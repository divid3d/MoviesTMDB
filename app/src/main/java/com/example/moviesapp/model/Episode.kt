package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Episode(
    val id: Int,
    @Json(name = "air_date")
    val airDate: Date?,
    @Json(name = "episode_number")
    val episodeNumber: Int,
    val name: String,
    val overview: String,
    @Json(name = "production_code")
    val productionCode: String,
    @Json(name = "season_number")
    val seasonNumber: Int,
    @Json(name = "still_path")
    val stillPath: String?,
    @Json(name = "vote_average")
    val voteAverage: Float,
    @Json(name = "vote_count")
    val voteCount: Int,
    @Transient
    val stillUrl: String? = null
)