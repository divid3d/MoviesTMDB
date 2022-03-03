package com.example.moviesapp.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.moviesapp.model.*
import com.example.moviesapp.utils.enqueueFileResponse
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named


@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class TmdbApiTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_okhttp")
    lateinit var client: OkHttpClient

    @Inject
    lateinit var moshi: Moshi

    private lateinit var mockWebServer: MockWebServer
    private lateinit var tmdbApi: TmdbApi

    @Before
    fun setup() {
        hiltRule.inject()

        mockWebServer = MockWebServer().apply {
            start(8080)
        }

        tmdbApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .build()
            .create(TmdbApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getConfigShouldResponseWithConfigOn200() = runTest {
        mockWebServer.enqueueFileResponse(
            "get_configuration_200.json",
            200
        )

        val actual = tmdbApi.getConfig().awaitResponse().body()
        val expected = Config(
            imagesConfig = ImagesConfig(
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
            ),
            changeKeys = listOf(
                "adult",
                "air_date",
                "also_known_as",
                "alternative_titles",
                "biography",
                "birthday",
                "budget",
                "cast",
                "certifications",
                "character_names",
                "created_by",
                "crew",
                "deathday",
                "episode",
                "episode_number",
                "episode_run_time",
                "freebase_id",
                "freebase_mid",
                "general",
                "genres",
                "guest_stars",
                "homepage",
                "images",
                "imdb_id",
                "languages",
                "name",
                "network",
                "origin_country",
                "original_name",
                "original_title",
                "overview",
                "parts",
                "place_of_birth",
                "plot_keywords",
                "production_code",
                "production_companies",
                "production_countries",
                "releases",
                "revenue",
                "runtime",
                "season",
                "season_number",
                "season_regular",
                "spoken_languages",
                "status",
                "tagline",
                "title",
                "translations",
                "tvdb_id",
                "tvrage_id",
                "type",
                "video",
                "videos"
            )
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getMoviesGenres() = runTest {
        mockWebServer.enqueueFileResponse(
            "get_movie_genres_200.json",
            200
        )

        val actual = tmdbApi.getMovieGenres(
            isoCode = DeviceLanguage.default.languageCode
        ).awaitResponse().body()

        val expected = GenresResponse(
            genres = listOf(
                Genre(28, "Akcja"),
                Genre(12, "Przygodowy"),
                Genre(16, "Animacja"),
                Genre(35, "Komedia"),
                Genre(80, "Kryminał"),
                Genre(99, "Dokumentalny"),
                Genre(18, "Dramat"),
                Genre(10751, "Familijny"),
                Genre(14, "Fantasy"),
                Genre(36, "Historyczny"),
                Genre(27, "Horror"),
                Genre(10402, "Muzyczny"),
                Genre(9648, "Tajemnica"),
                Genre(10749, "Romans"),
                Genre(878, "Sci-Fi"),
                Genre(10770, "film TV"),
                Genre(53, "Thriller"),
                Genre(10752, "Wojenny"),
                Genre(37, "Western")
            )
        )

        assertThat(actual).isEqualTo(expected)
    }

}