package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class TvSeriesDetails(
    override val id: Int,
    @SerializedName("backdrop_path")
    override val backdropPath: String?,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("created_by")
    val creators: List<Creator>,
    val homepage: String,
    val genres: List<Genre>,
    @SerializedName("in_production")
    val inProduction: Boolean,
    val languages: List<String>,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("last_air_date")
    val lastAirDate: String?,
    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: Episode,
    val name: String,
    @SerializedName("next_episode_to_air")
    val nextEpisodeToAir: Episode?,
    val networks: List<Network>,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String?,
    override val overview: String,
    val popularity: Float,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>,
    val seasons: List<Season>,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val type: String,
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
) : Presentable {
    override val title: String
        get() = name
}