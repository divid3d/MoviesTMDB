package com.example.moviesapp.other

import com.example.moviesapp.model.*

fun Config.getImageUrl(
    imagePath: String?,
    size: String = "w500"
): String? {
    return imagePath?.let { path ->
        val baseUrl = imagesConfig.secureBaseUrl
        //val smallestSize = imagesConfig.backdropSizes.first()

        "${baseUrl}${size}${path}"
    }
}

fun TvSeries.appendUrls(
    config: Config?
): TvSeries {
    val tvSeriesPosterUrl = config?.getImageUrl(posterPath)
    val tvSeriesBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

    return copy(
        posterUrl = tvSeriesPosterUrl,
        backdropUrl = tvSeriesBackdropUrl,

        )
}

fun Creator.appendUrls(
    config: Config?
): Creator {
    val profileUrl = config?.getImageUrl(profilePath, size = "w185")

    return copy(
        profileUrl = profileUrl
    )
}

fun Episode.appendUrls(
    config: Config?
): Episode {
    val stillUrl = config?.getImageUrl(stillPath, size = "w300")

    return copy(
        stillUrl = stillUrl
    )
}

fun Season.appendUrl(
    config: Config?
): Season {
    val posterUrl = config?.getImageUrl(posterPath)

    return copy(
        posterUrl = posterUrl
    )
}

fun TvSeriesFavourite.appendUrls(
    config: Config?
): TvSeriesFavourite {
    val moviePosterUrl = config?.getImageUrl(posterPath)
    val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

    return copy(
        posterUrl = moviePosterUrl,
        backdropUrl = movieBackdropUrl
    )
}