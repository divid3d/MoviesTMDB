package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val page: Int,
    @SerializedName("results")
    val results: List<SearchResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)