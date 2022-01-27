package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    override val id: Int,

    @SerializedName("name")
    private val tvSeriesName: String?,

    @SerializedName("title")
    private val movieTitle: String?,

    @SerializedName("media_type")
    val mediaType: MediaType,

    override val overview: String,

    @SerializedName("poster_path")
    override val posterPath: String?,

    @Transient
    override val posterUrl: String?
) : Presentable {
    override val backdropPath: String?
        get() = null
    override val voteAverage: Float
        get() = 0f
    override val backdropUrl: String?
        get() = null
    override val voteCount: Int
        get() = 0

    override val title: String
        get() = when {
            !movieTitle.isNullOrEmpty() -> movieTitle
            !tvSeriesName.isNullOrEmpty() -> tvSeriesName
            else -> ""
        }
        
}