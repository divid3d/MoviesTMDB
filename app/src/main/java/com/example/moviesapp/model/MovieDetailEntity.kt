package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetailEntity(
    override val id: Int,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    override val title: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    override val adult: Boolean?,

    override val overview: String,

    @ColumnInfo(name = "backdrop_path")
    override val backdropPath: String?,

    override val voteAverage: Float,

    override val voteCount: Int,
) : DetailPresentable {
    @PrimaryKey(autoGenerate = true)
    var entityId: Int = 0
}