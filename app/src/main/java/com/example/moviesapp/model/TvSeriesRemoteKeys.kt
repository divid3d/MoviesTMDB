package com.example.moviesapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["language", "type"])])
data class TvSeriesRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val language: String,
    val type: TvSeriesEntityType,
    val nextPage: Int?,
    val lastUpdated: Long
)