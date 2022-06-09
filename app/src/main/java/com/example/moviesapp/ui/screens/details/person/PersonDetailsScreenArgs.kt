package com.example.moviesapp.ui.screens.details.person

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonDetailsScreenArgs(
    val personId: Int,
    val startRoute: String
) : Parcelable