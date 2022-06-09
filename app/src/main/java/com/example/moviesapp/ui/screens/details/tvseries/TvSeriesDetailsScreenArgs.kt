package com.example.moviesapp.ui.screens.details.tvseries

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvSeriesDetailsScreenArgs(
    val tvSeriesId: Int,
    val startRoute: String
) : Parcelable