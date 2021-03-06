package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvSeasonsResponse(
    override val id: Int,

    @Json(name = "air_date")
    val airDate: String?,

    val name: String,

    val overview: String,

    @Json(name = "poster_path")
    override val posterPath: String?,

    @Json(name = "season_number")
    val seasonNumber: Int,

    val episodes: List<Episode>
) : Presentable {

    override val title: String = name
}