package com.example.moviesapp.other

import android.util.Size
import com.example.moviesapp.model.*
import kotlin.math.abs

class ImageUrlParser(private val imagesConfig: ImagesConfig) {
    private val secureBaseUrl = imagesConfig.secureBaseUrl

    private val backdropDimensions = convertCodes(imagesConfig.backdropSizes)
    private val logoDimensions = convertCodes(imagesConfig.logoSizes)
    private val posterDimensions = convertCodes(imagesConfig.posterSizes)
    private val profileDimensions = convertCodes(imagesConfig.profileSizes)
    private val stillDimensions = convertCodes(imagesConfig.stillSizes)

    fun getImageUrl(
        path: String?,
        type: ImageType,
        preferredSize: Size? = null,
        strategy: MatchingStrategy = MatchingStrategy.FirstBiggerWidth
    ): String? {
        val source = when (type) {
            ImageType.Backdrop -> backdropDimensions
            ImageType.Logo -> logoDimensions
            ImageType.Poster -> posterDimensions
            ImageType.Profile -> profileDimensions
            ImageType.Still -> stillDimensions
        }

        return urlFromSource(source, path, preferredSize, strategy)
    }

    private fun urlFromSource(
        source: List<Dimension>,
        path: String?,
        preferredSize: Size?,
        strategy: MatchingStrategy = MatchingStrategy.FirstBiggerWidth
    ): String? {
        if (path == null) {
            return null
        }

        if (preferredSize == null) {
            return createSecureUrl(secureBaseUrl, Dimension.Original, path)
        }

        val preferredDimension = when (strategy) {
            MatchingStrategy.FirstBiggerWidth -> {
                source.filterIsInstance<Dimension.Width>()
                    .firstOrNull { dimension -> dimension.value >= preferredSize.width }
            }

            MatchingStrategy.FirstBiggerHeight -> {
                source.filterIsInstance<Dimension.Height>()
                    .firstOrNull { dimension -> dimension.value >= preferredSize.height }
            }

            MatchingStrategy.LowestWidthDiff -> {
                source.filterIsInstance<Dimension.Width>().map { dimension ->
                    dimension to abs(preferredSize.width - dimension.value)
                }.minByOrNull { (_, delta) -> delta }?.first
            }

            MatchingStrategy.LowestHeightDiff -> {
                source.filterIsInstance<Dimension.Height>().map { dimension ->
                    dimension to abs(preferredSize.height - dimension.value)
                }.minByOrNull { (_, delta) -> delta }?.first
            }
        } ?: Dimension.Original

        return createSecureUrl(secureBaseUrl, preferredDimension, path)
    }

    private sealed class Dimension(val code: String) {
        object Original : Dimension(code = "original")
        data class Width(val value: Int) : Dimension(code = code) {
            companion object {
                val code: String
                    get() = "w"
            }
        }

        data class Height(val value: Int) : Dimension(code = code) {
            companion object {
                val code: String
                    get() = "h"
            }
        }

        fun asCode() = when (this) {
            is Width -> "${Width.code}${this.value}"
            is Height -> "${Height.code}${this.value}"
            is Original -> "original"
        }
    }


    private fun convertCodes(codes: List<String>): List<Dimension> {
        return codes.mapNotNull { code ->
            when {
                code.contains(Dimension.Original.code) -> Dimension.Original
                code.contains(Dimension.Width.code) -> {
                    val value = getValueFromCode(code)

                    if (value != null) Dimension.Width(value) else null

                }
                code.contains(Dimension.Height.code) -> {
                    val value = getValueFromCode(code)

                    if (value != null) Dimension.Height(value) else null
                }
                else -> null
            }
        }
    }

    private fun getValueFromCode(code: String): Int? {
        return code.filter { char -> char.isDigit() }.toIntOrNull()
    }

    private fun createSecureUrl(secureBaseUrl: String, dimension: Dimension, path: String): String {
        return "${secureBaseUrl}${dimension.asCode()}${path}"
    }

    enum class MatchingStrategy {
        FirstBiggerWidth, FirstBiggerHeight, LowestWidthDiff, LowestHeightDiff
    }

    enum class ImageType {
        Backdrop, Logo, Poster, Profile, Still
    }
}

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

