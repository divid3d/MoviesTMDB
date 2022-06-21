package com.example.moviesapp.ui.screens.seasons

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeasonDetailsScreenArgs(
    val tvSeriesId: Int,
    val seasonNumber: Int,
    val startRoute: String
) : Parcelable