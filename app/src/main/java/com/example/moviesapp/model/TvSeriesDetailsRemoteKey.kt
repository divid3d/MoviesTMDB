package com.example.moviesapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["language"])])
data class TvSeriesDetailsRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val language: String,
    val nextPage: Int?,
    val lastUpdated: Long
)