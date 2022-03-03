package com.example.moviesapp.other

import android.util.Size
import com.example.moviesapp.model.ImagesConfig
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ImageUrlParserTest {
    private lateinit var imageUrlParser: ImageUrlParser

    @Before
    fun setup() {
        val imagesConfig = ImagesConfig(
            baseUrl = "http://image.tmdb.org/t/p/",
            secureBaseUrl = "https://image.tmdb.org/t/p/",
            backdropSizes = listOf(
                "w300",
                "w780",
                "w1280",
                "original"
            ),
            logoSizes = listOf(
                "w45",
                "w92",
                "w154",
                "w185",
                "w300",
                "w500",
                "original"
            ),
            posterSizes = listOf(
                "w92",
                "w154",
                "w185",
                "w342",
                "w500",
                "w780",
                "original"
            ),
            profileSizes = listOf(
                "w45",
                "w185",
                "h632",
                "original"
            ),
            stillSizes = listOf(
                "w92",
                "w185",
                "w300",
                "original"
            )
        )

        imageUrlParser = ImageUrlParser(imagesConfig)
    }

    @Test
    fun posterImageUrlWithNullPath_shouldBeNull() {
        val posterPath = null
        val type = ImageUrlParser.ImageType.Poster
        val size = Size(100, 160)
        val strategy = ImageUrlParser.MatchingStrategy.FirstBiggerWidth

        val actualImageUrl = imageUrlParser.getImageUrl(
            path = posterPath,
            type = type,
            preferredSize = size,
            strategy = strategy
        )

        assertThat(actualImageUrl).isEqualTo(null)
    }

    @Test
    fun posterImageUrlWithWidthOf50_shouldBeValid() {
        val posterPath = "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"
        val type = ImageUrlParser.ImageType.Poster
        val size = Size(50, 80)
        val strategy = ImageUrlParser.MatchingStrategy.FirstBiggerWidth

        val actualImageUrl = imageUrlParser.getImageUrl(
            path = posterPath,
            type = type,
            preferredSize = size,
            strategy = strategy
        )

        val expectedImageUrl = "https://image.tmdb.org/t/p/w92/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"

        assertThat(actualImageUrl).isEqualTo(expectedImageUrl)
    }

    @Test
    fun posterImageUrlWithWidthOf100_shouldBeValid() {
        val posterPath = "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"
        val type = ImageUrlParser.ImageType.Poster
        val size = Size(100, 160)
        val strategy = ImageUrlParser.MatchingStrategy.FirstBiggerWidth

        val actualImageUrl = imageUrlParser.getImageUrl(
            path = posterPath,
            type = type,
            preferredSize = size,
            strategy = strategy
        )

        val expectedImageUrl = "https://image.tmdb.org/t/p/w154/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"

        assertThat(actualImageUrl).isEqualTo(expectedImageUrl)
    }

    @Test
    fun posterImageUrlWithHugeWidth_shouldBeValid() {
        val posterPath = "/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"
        val type = ImageUrlParser.ImageType.Poster
        val size = Size(1000, 1600)
        val strategy = ImageUrlParser.MatchingStrategy.FirstBiggerWidth

        val actualImageUrl = imageUrlParser.getImageUrl(
            path = posterPath,
            type = type,
            preferredSize = size,
            strategy = strategy
        )

        val expectedImageUrl = "https://image.tmdb.org/t/p/original/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"

        assertThat(actualImageUrl).isEqualTo(expectedImageUrl)
    }

}