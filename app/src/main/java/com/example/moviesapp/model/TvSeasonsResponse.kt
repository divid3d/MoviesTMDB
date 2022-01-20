package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class TvSeasonsResponse(
    override val id: Int,
    @SerializedName("air_date")
    val airDate: String?,
    val name: String,
    override val overview: String,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int,
    val episodes: List<Episode>,
    @Transient
    override val posterUrl: String?
) : Presentable {
    override val title: String
        get() = name
    override val backdropPath: String?
        get() = null
    override val voteAverage: Float
        get() = 0f
    override val backdropUrl: String?
        get() = null
    override val voteCount: Int
        get() = 0
}