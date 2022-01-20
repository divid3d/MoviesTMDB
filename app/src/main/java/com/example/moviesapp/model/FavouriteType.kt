package com.example.moviesapp.model

import androidx.annotation.StringRes
import com.example.moviesapp.R

enum class FavouriteType {
    Movie, TvSeries;

    @StringRes
    fun getLabelResourceId() = when (this) {
        Movie -> R.string.favourite_type_movie_label
        TvSeries -> R.string.favourite_type_tv_series_label
    }
}