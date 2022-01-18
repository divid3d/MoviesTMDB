package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Season(
    val id: Int,
    @SerializedName("air_date")
    val airDate: Date?,
    val name: String,
    val overview: String,
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    @Transient
    val posterUrl: String?
)