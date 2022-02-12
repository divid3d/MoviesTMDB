package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    override val id: Int,

    @Json(name = "name")
    val tvSeriesName: String?,

    @Json(name = "title")
    val movieTitle: String?,

    @Json(name = "media_type")
    val mediaType: MediaType,

    override val overview: String?,

    @Json(name = "poster_path")
    override val posterPath: String?
) : Presentable {
    override val backdropPath: String?
        get() = null

    override val voteAverage: Float
        get() = 0f

    override val voteCount: Int
        get() = 0

    override val adult: Boolean?
        get() = null

    override val title: String
        get() = when {
            !movieTitle.isNullOrEmpty() -> movieTitle
            !tvSeriesName.isNullOrEmpty() -> tvSeriesName
            else -> ""
        }
}