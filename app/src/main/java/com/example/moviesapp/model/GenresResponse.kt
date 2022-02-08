package com.example.moviesapp.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GenresResponse(
    val genres: List<Genre>
)