package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Part(
    override val id: Int,

    override val adult: Boolean?,

    @Json(name = "genre_ids")
    val genreIds: List<Int>,

    @Json(name = "original_language")
    val originalLanguage: String?,

    @Json(name = "original_title")
    val originalTitle: String?,

    override val overview: String?,

    @Json(name = "release_date")
    val releaseDate: String?,

    @Json(name = "poster_path")
    override val posterPath: String?,

    val popularity: Float,

    override val title: String,

    val video: Boolean,

    @Json(name = "vote_average")
    override val voteAverage: Float,

    @Json(name = "vote_count")
    override val voteCount: Int,

    @Transient
    override val posterUrl: String? = null,

    ) : Presentable {
    override val backdropPath: String?
        get() = null
    override val backdropUrl: String?
        get() = null
}