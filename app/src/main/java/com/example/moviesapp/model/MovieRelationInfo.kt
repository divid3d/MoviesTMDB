package com.example.moviesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieRelationInfo(
    val type: RelationType,
    val movieId: Int
) : Parcelable