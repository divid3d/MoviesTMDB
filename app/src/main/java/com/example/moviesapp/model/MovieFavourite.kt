package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MovieFavourite(
    @PrimaryKey
    override val id: Int,

    @ColumnInfo(name = "backdrop_path")
    override val backdropPath: String?,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    override val title: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    override val overview: String,

    @ColumnInfo(name = "vote_average")
    override val voteAverage: Float,

    @ColumnInfo(name = "vote_count")
    override val voteCount: Int,

    @ColumnInfo(name = "added_date")
    val addedDate: Date?,

    @Ignore
    override val backdropUrl: String?,

    @Ignore
    override val posterUrl: String?
) : Presentable {
    constructor(
        id: Int,
        backdropPath: String?,
        posterPath: String?,
        title: String,
        originalTitle: String,
        overview: String,
        voteAverage: Float,
        voteCount: Int,
        addedDate: Date
    ) : this(
        id,
        backdropPath,
        posterPath,
        title,
        originalTitle,
        overview,
        voteAverage,
        voteCount,
        addedDate,
        null,
        null
    )
}