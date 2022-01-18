package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Episode(
    val id: Int,
    @SerializedName("air_date")
    val airDate: Date?,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    val name: String,
    val overview: String,
    @SerializedName("production_code")
    val productionCode: String,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("still_path")
    val stillPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int,
    @Transient
    val stillUrl: String?
)