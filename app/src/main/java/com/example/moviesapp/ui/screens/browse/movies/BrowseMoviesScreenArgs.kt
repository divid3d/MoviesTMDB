package com.example.moviesapp.ui.screens.browse.movies

import android.os.Parcelable
import com.example.moviesapp.model.MovieType
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrowseMoviesScreenArgs(
    val movieType: MovieType
) : Parcelable