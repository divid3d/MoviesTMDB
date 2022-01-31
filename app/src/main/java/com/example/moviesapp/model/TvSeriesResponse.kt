package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvSeriesResponse(
    val page: Int,

    @Json(name = "results")
    val tvSeries: List<TvSeries>,

    @Json(name = "total_pages")
    val totalPages: Int,

    @Json(name = "total_results")
    val totalResults: Int
)