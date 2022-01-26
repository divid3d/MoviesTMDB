package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class SeasonDetails(
    @SerializedName("air_date")
    val airDate: Date?,

    val episodes: List<Episode>,

    val name: String,

    override val overview: String,

    override val id: Int,

    @SerializedName("season_number")
    val seasonNumber: Int,

    @SerializedName("poster_path")
    override val posterPath: String?,

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