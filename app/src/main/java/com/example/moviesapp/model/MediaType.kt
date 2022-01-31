package com.example.moviesapp.model

import com.squareup.moshi.Json

enum class MediaType(val value: String) {
    @Json(name = "movie")
    Movie("movie"),

    @Json(name = "tv")
    Tv("tv"),

    @Json(name = "person")
    Person("person")
}