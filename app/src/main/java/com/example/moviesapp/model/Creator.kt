package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class Creator(
    val id: Int,
    @SerializedName("credit_id")
    val creditId: String,
    val name: String,
    val gender: Int,
    @SerializedName("profile_path")
    val profilePath: String?,
    @Transient
    val profileUrl: String?
)