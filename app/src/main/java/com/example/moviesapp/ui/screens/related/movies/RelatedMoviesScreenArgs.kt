package com.example.moviesapp.ui.screens.related.movies

import android.os.Parcelable
import com.example.moviesapp.model.RelationType
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelatedMoviesScreenArgs(
    val movieId: Int,
    val type: RelationType,
    val startRoute: String
) : Parcelable