package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class Backdrop(
    @SerializedName("aspect_ratio")
    val aspectRatio: Float,

    @SerializedName("file_path")
    val filePath: String,

    val height: Int,

    val width: Int,

    @SerializedName("vote_average")
    val voteAverage: Float,

    @SerializedName("vote_count")
    val voteCount: Int,

    @Transient
    val fileUrl: String?
)