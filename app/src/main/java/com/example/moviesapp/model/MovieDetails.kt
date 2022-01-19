package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class MovieDetails(
    override val id: Int,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    override val backdropPath: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    override val overview: String,
    val popularity: Float,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date")
    val releaseDate: Date,
    val revenue: Int,
    val runtime: Int?,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    override val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    override val voteAverage: Float,
    @SerializedName("vote_count")
    override val voteCount: Int,
    @Transient
    override val posterUrl: String?,
    @Transient
    override val backdropUrl: String?,
    @Transient
    val isFavourite: Boolean?
) : Presentable

