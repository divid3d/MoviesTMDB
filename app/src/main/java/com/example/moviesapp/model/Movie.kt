package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Movie(
    override val id: Int,

    @Json(name = "poster_path")
    override val posterPath: String?,

    val adult: Boolean,

    override val overview: String,

    @Json(name = "release_date")
    val releaseDate: String?,

    @Json(name = "genre_ids")
    val genreIds: List<Int>,

    @Json(name = "original_title")
    val originalTitle: String,

    @Json(name = "original_language")
    val originalLanguage: String,

    override val title: String,

    @Json(name = "backdrop_path")
    override val backdropPath: String?,

    val popularity: Float,

    @Json(name = "vote_count")
    override val voteCount: Int,

    val video: Boolean,

    @Json(name = "vote_average")
    override val voteAverage: Float,

    @Transient
    override val posterUrl: String? = null,

    @Transient
    override val backdropUrl: String? = null
) : Presentable