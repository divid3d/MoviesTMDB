package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

enum class MediaType(val value: String) {
    @SerializedName("movie")
    Movie("movie"),

    @SerializedName("tv")
    Tv("tv"),

    @SerializedName("person")
    Person("person")
}