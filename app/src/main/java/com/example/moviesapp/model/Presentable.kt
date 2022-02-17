package com.example.moviesapp.model

interface Presentable {
    val id: Int
    val title: String
    val posterPath: String?
}

interface DetailPresentable : Presentable {
    val adult: Boolean?
    val overview: String?
    val backdropPath: String?
    val voteAverage: Float
    val voteCount: Int
}