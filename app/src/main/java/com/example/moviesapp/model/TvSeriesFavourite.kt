package com.example.moviesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TvSeriesFavourite(
    @PrimaryKey
    override val id: Int,

    @ColumnInfo(name = "poster_path")
    override val posterPath: String?,

    val name: String,

    @ColumnInfo(name = "added_date")
    val addedDate: Date
) : Presentable {

    override val title: String
        get() = name
}