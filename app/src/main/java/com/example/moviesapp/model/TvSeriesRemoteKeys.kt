package com.example.moviesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TvSeriesRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val type: TvSeriesEntityType,
    val nextPage: Int?,
    val lastUpdated: Long
)