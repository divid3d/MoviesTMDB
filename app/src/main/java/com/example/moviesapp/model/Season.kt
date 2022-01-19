package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class Season(
    val id: Int,
    @SerializedName("air_date")
    val airDate: String?,
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