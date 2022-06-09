package com.example.moviesapp.ui.screens.browse.tvseries

import android.os.Parcelable
import com.example.moviesapp.model.TvSeriesType
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrowseTvSeriesScreenArgs(
    val tvSeriesType: TvSeriesType
) : Parcelable