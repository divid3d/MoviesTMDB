package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MovieFavourite(
    @PrimaryKey
    override val id: Int,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    override val title: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "added_date")
    val addedDate: Date?
) : Presentable