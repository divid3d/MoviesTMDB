package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class TvSeasonsResponse(
    val id: Int,
    @SerializedName("air_date")
    val airDate: String?,
    val name: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int,
    val episodes: List<Episode>,
    @Transient
    val posterUrl: String?
)