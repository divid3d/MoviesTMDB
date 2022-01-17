package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("images")
    val imagesConfig: ImagesConfig,

    @SerializedName("change_keys")
    val changeKeys: List<String>
)

