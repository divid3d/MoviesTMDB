package com.example.moviesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviesRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val language: String,
    val type: MovieEntityType,
    val nextPage: Int?,
    val lastUpdated: Long
)