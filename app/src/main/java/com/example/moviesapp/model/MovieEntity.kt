package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    override val id: Int,

    val type: MovieEntityType,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    override val title: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,


) : Presentable {
    @PrimaryKey(autoGenerate = true)
    var entityId: Int = 0
}


enum class MovieEntityType{
    Discover, Upcoming, Trending, TopRated, Popular
}