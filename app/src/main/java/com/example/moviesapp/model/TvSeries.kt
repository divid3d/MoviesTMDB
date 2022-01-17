package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class TvSeries(
    override val id: Int,
    @SerializedName("poster_path")
    override val posterPath: String?,
    override val overview: String,
    @SerializedName("first_air_date")
    val firstAirDate: Date,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("origin_country")
    val originCountry: List<String>,
    val name: String,
    @SerializedName("backdrop_path")
    override val backdropPath: String?,
    val popularity: Float,
    @SerializedName("vote_count")
    override val voteCount: Int,
    @SerializedName("vote_average")
    override val voteAverage: Float,
    @Transient
    override val posterUrl: String?,
    @Transient
    override val backdropUrl: String?
) : Presentable {
    override val title: String
        get() = name
}