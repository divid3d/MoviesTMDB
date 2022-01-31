package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvSeriesDetails(
    override val id: Int,

    @Json(name = "backdrop_path")
    override val backdropPath: String?,

    @Json(name = "poster_path")
    override val posterPath: String?,

    @Json(name = "created_by")
    val creators: List<Creator>,

    val homepage: String,

    val genres: List<Genre>,

    @Json(name = "in_production")
    val inProduction: Boolean,

    val languages: List<String>,

    @Json(name = "first_air_date")
    val firstAirDate: String?,

    @Json(name = "last_air_date")
    val lastAirDate: String?,

    @Json(name = "last_episode_to_air")
    val lastEpisodeToAir: Episode,

    val name: String,

    @Json(name = "next_episode_to_air")
    val nextEpisodeToAir: Episode?,

    val networks: List<Network>,

    @Json(name = "number_of_episodes")
    val numberOfEpisodes: Int,

    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int,

    @Json(name = "origin_country")
    val originCountry: List<String>?,

    @Json(name = "original_language")
    val originalLanguage: String,

    @Json(name = "original_name")
    val originalName: String?,

    override val overview: String,

    val popularity: Float,

    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany>,

    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountry>,

    val seasons: List<Season>,

    @Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,

    val status: String,

    val tagline: String,

    val type: String,

    @Json(name = "vote_average")
    override val voteAverage: Float,

    @Json(name = "vote_count")
    override val voteCount: Int,

    @Transient
    override val posterUrl: String? = null,

    @Transient
    override val backdropUrl: String? = null,
) : Presentable {
    override val title: String
        get() = name
}