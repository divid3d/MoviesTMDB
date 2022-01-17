package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class Movie(
    override val id: Int,
    @SerializedName("poster_path")
    override val posterPath: String?,
    val adult: Boolean,
    override val overview: String,
    @SerializedName("release_date")
    val releaseDate: Date,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    override val title: String,
    @SerializedName("backdrop_path")
    override val backdropPath: String?,
    val popularity: Float,
    @SerializedName("vote_count")
    override val voteCount: Int,
    val video: Boolean,
    @SerializedName("vote_average")
    override val voteAverage: Float,
    @Transient
    override val posterUrl: String?,
    @Transient
    override val backdropUrl: String?
) : Presentable