package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TvSeriesFavourite(
    @PrimaryKey
    override val id: Int,

    @ColumnInfo(name = "backdrop_path")
    override val backdropPath: String?,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    val name: String,

    override val overview: String,

    @ColumnInfo(name = "vote_average")
    override val voteAverage: Float,

    @ColumnInfo(name = "vote_count")
    override val voteCount: Int,

    @ColumnInfo(name = "added_date")
    val addedDate: Date?
) : Presentable {
    override val adult: Boolean?
        get() = null

    override val title: String
        get() = name
}