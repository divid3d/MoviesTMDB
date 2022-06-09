package com.example.moviesapp.ui.screens.related.tvseries

import android.os.Parcelable
import com.example.moviesapp.model.RelationType
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelatedTvSeriesScreenArgs(
    val tvSeriesId: Int,
    val type: RelationType,
    val startRoute: String
) : Parcelable