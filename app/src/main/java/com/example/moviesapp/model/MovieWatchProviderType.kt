package com.example.moviesapp.model

import com.example.moviesapp.R

enum class MovieWatchProviderType {
    Stream, Rent, Buy;

    fun getLabelResId() = when (this) {
        Rent -> R.string.movie_provider_type_rent_label
        Buy -> R.string.movie_provider_type_buy_label
        Stream -> R.string.movie_provider_type_stream_label
    }
}