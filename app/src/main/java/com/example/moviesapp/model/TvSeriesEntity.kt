package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["type", "language"])])
data class TvSeriesEntity(
    override val id: Int,

    val type: TvSeriesEntityType,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    override val title: String,

    @ColumnInfo(name = "original_name")
    val originalName: String?,

    val language: String
) : Presentable {
    @PrimaryKey(autoGenerate = true)
    var entityId: Int = 0
}


enum class TvSeriesEntityType {
    Trending, TopRated, AiringToday, Popular, Discover
}