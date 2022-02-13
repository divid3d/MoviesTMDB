package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExternalIds(
    val id: Int,

    @Json(name = "imdb_id")
    val imdbId: String?,

    @Json(name = "facebook_id")
    val facebookId: String?,

    @Json(name = "freebase_mid")
    val freebaseMid: String?,

    @Json(name = "freebase_id")
    val freebaseId: String?,

    @Json(name = "instagram_id")
    val instagramId: String?,

    @Json(name = "tvrage_id")
    val tvRageId: String?,

    @Json(name = "twitter_id")
    val twitterId: String?
)