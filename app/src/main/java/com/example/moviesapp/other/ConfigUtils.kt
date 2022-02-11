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

fun Movie.appendUrls(
    config: Config?
): Movie {
    val moviePosterUrl = config?.getImageUrl(posterPath)
    val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

    return copy(
        posterUrl = moviePosterUrl,
        backdropUrl = movieBackdropUrl
    )
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
    val stillUrl = config?.getImageUrl(stillPath, size = "original")

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

fun TvSeasonsResponse.appendUrl(
    config: Config?
): TvSeasonsResponse {
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

fun MovieFavourite.appendUrls(
    config: Config?
): MovieFavourite {
    val moviePosterUrl = config?.getImageUrl(posterPath)
    val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

    return copy(
        posterUrl = moviePosterUrl,
        backdropUrl = movieBackdropUrl
    )
}

fun RecentlyBrowsedMovie.appendUrls(
    config: Config?
): RecentlyBrowsedMovie {
    val moviePosterUrl = config?.getImageUrl(posterPath)
    val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

    return copy(
        posterUrl = moviePosterUrl,
        backdropUrl = movieBackdropUrl
    )
}

fun RecentlyBrowsedTvSeries.appendUrls(
    config: Config?
): RecentlyBrowsedTvSeries {
    val moviePosterUrl = config?.getImageUrl(posterPath)
    val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

    return copy(
        posterUrl = moviePosterUrl,
        backdropUrl = movieBackdropUrl
    )
}

fun Network.appendUrls(
    config: Config?
): Network {
    val logoUrl = config?.getImageUrl(logoPath, size = "w300")

    return copy(
        logoUrl = logoUrl
    )
}

fun SeasonDetails.appendUrls(
    config: Config?
): SeasonDetails {
    val posterUrl = config?.getImageUrl(posterPath)

    return copy(
        posterUrl = posterUrl,
        episodes = episodes.map { episode -> episode.appendUrls(config) }
    )
}

fun SearchResult.appendUrls(
    config: Config?
): SearchResult {
    val posterUrl = config?.getImageUrl(posterPath)

    return copy(
        posterUrl = posterUrl
    )
}

fun Image.appendUrls(
    config: Config?
): Image {
    val url = config?.getImageUrl(filePath, size = "original")

    return copy(
        fileUrl = url
    )
}

fun AuthorDetails.appendUrls(
    config: Config?
): AuthorDetails {
    val avatarUrl = config?.getImageUrl(avatarPath, size = "w185")

    return copy(
        avatarUrl = avatarUrl
    )
}

fun Part.appendUrls(
    config: Config?
): Part {
    val posterUrl = config?.getImageUrl(posterPath)

    return copy(
        posterUrl = posterUrl
    )
}

fun PersonDetails.appendUrls(
    config: Config?
): PersonDetails {
    val profileUrl = config?.getImageUrl(profilePath, size = "w185")

    return copy(
        profileUrl = profileUrl
    )
}

fun CombinedCreditsCast.appendUrls(
    config: Config?
): CombinedCreditsCast {
    val posterUrl = config?.getImageUrl(posterPath)

    return copy(
        posterUrl = posterUrl
    )
}

fun CombinedCreditsCrew.appendUrls(
    config: Config?
): CombinedCreditsCrew {
    val posterUrl = config?.getImageUrl(posterPath, size = "original")

    return copy(
        posterUrl = posterUrl
    )
}

