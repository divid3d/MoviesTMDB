package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieWatchProvidersResponse(
    val id: Int,
    val results: Map<String, MovieWatchProviders>
)

@JsonClass(generateAdapter = true)
data class MovieWatchProviders(
    val link: String,

    val rent: List<ProviderSource>?,

    val buy: List<ProviderSource>?,

    val flatrate: List<ProviderSource>?
)

@JsonClass(generateAdapter = true)
data class ProviderSource(
    @Json(name = "display_priority")
    val displayPriority: Int,

    @Json(name = "logo_path")
    val logoPath: String,

    @Json(name = "provider_id")
    val providerId: Int,

    @Json(name = "provider_name")
    val providerName: String
)