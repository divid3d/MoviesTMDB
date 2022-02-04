package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RecentlyBrowsedTvSeries(
    @PrimaryKey
    override val id: Int,

    @ColumnInfo(name = "backdrop_path")
    override val backdropPath: String?,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    val name: String,

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
        name: String,
        addedDate: Date,
    ) : this(
        id,
        backdropPath,
        posterPath,
        name,
        addedDate,
        null,
        null
    )

    override val adult: Boolean?
        get() = null

    override val title: String
        get() = name

    override val overview: String?
        get() = null
    override val voteAverage: Float
        get() = 0f
    override val voteCount: Int
        get() = 0
}