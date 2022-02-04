package com.example.moviesapp.model

interface Presentable {
    val id: Int
    val adult: Boolean?
    val title: String
    val overview: String?
    val posterPath: String?
    val backdropPath: String?
    val voteAverage: Float
    val posterUrl: String?
    val backdropUrl: String?
    val voteCount: Int
}