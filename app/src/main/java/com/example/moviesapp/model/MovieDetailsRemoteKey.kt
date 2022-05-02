package com.example.moviesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetailsRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val nextPage: Int?,
    val lastUpdated: Long
)