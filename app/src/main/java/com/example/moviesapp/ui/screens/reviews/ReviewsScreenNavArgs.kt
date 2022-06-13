package com.example.moviesapp.ui.screens.reviews

import android.os.Parcelable
import com.example.moviesapp.model.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewsScreenNavArgs(
    val startRoute: String,
    val mediaId: Int,
    val type: MediaType
) : Parcelable