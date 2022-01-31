package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class RecentlyBrowsedMovie(
    @PrimaryKey
    override val id: Int,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    override val title: String,

    @ColumnInfo(name = "backdrop_path")
    override val backdropPath: String?,

    @ColumnInfo(name = "added_date")
    val addedDate: Date?,

    @Transient
    override val posterUrl: String? = null,

    @Transient
    override val backdropUrl: String? = null
) : Presentable {
    constructor(
        id: Int,
        posterPath: String?,
        title: String,
        backdropPath: String?,
        addedDate: Date?
    ) : this(
        id,
        posterPath,
        title,
        backdropPath,
        addedDate,
        null,
        null
    )

    override val overview: String?
        get() = null
    override val voteAverage: Float
        get() = 0f
    override val voteCount: Int
        get() = 0
}