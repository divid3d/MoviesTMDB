package com.example.moviesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvSeriesRelationInfo(
    val type: RelationType,
    val tvSeriesId: Int
) : Parcelable