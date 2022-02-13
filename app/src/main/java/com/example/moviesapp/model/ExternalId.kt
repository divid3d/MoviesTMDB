package com.example.moviesapp.model

import androidx.annotation.DrawableRes
import com.example.moviesapp.R

sealed class ExternalId(@DrawableRes val drawableRes: Int) {
    data class Imdb(val id: String) :
        ExternalId(drawableRes = R.drawable.ic_imdb)

    data class Facebook(val id: String) :
        ExternalId(drawableRes = R.drawable.ic_facebook)

    data class Instagram(val id: String) :
        ExternalId(drawableRes = R.drawable.ic_instagram)

    data class Twitter(val id: String) :
        ExternalId(drawableRes = R.drawable.ic_twitter)
}