package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("iso_639_1")
    val iso: String,
    val name: String
)