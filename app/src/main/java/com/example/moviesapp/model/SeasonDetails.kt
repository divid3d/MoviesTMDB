package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class SeasonDetails(
    @Json(name = "air_date")
    val airDate: Date?,

    val episodes: List<Episode>,

    val name: String,

    override val overview: String,

    override val id: Int,

    @Json(name = "season_number")
    val seasonNumber: Int,

    @Json(name = "poster_path")
    override val posterPath: String?,

    @Transient
    override val posterUrl: String? = null
) : Presentable {
    override val adult: Boolean?
        get() = null
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