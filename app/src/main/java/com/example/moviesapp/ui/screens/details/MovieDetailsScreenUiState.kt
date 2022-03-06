package com.example.moviesapp.ui.screens.details

import androidx.paging.PagingData
import com.example.moviesapp.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.*

data class MovieDetailsScreenUiState(
    val startRoute: String,
    val movieDetails: MovieDetails?,
    val additionalMovieDetailsInfo: AdditionalMovieDetailsInfo,
    val associatedMovies: AssociatedMovies,
    val associatedContent: AssociatedContent,
    val error: String?
) {
    companion object {
        fun getDefault(startRoute: String): MovieDetailsScreenUiState {
            return MovieDetailsScreenUiState(
                startRoute = startRoute,
                movieDetails = null,
                additionalMovieDetailsInfo = AdditionalMovieDetailsInfo.default,
                associatedMovies = AssociatedMovies.default,
                associatedContent = AssociatedContent.default,
                error = null
            )
        }
    }
}

data class AssociatedMovies(
    val collection: MovieCollection?,
    val similar: Flow<PagingData<Movie>>,
    val recommendations: Flow<PagingData<Movie>>,
    val directorMovies: DirectorMovies
) {
    companion object {
        val default: AssociatedMovies
            get() = AssociatedMovies(
                collection = null,
                similar = emptyFlow(),
                recommendations = emptyFlow(),
                directorMovies = DirectorMovies.default
            )
    }
}

data class DirectorMovies(
    val directorName: String,
    val movies: Flow<PagingData<Movie>>
) {
    companion object {
        val default: DirectorMovies
            get() = DirectorMovies(
                directorName = "",
                movies = emptyFlow()
            )
    }
}

data class AdditionalMovieDetailsInfo(
    val isFavourite: Boolean,
    val watchAtTime: Date?,
    val watchProviders: WatchProviders?,
    val credits: Credits?,
    val hasReviews: Boolean
) {
    companion object {
        val default: AdditionalMovieDetailsInfo
            get() = AdditionalMovieDetailsInfo(
                isFavourite = false,
                watchAtTime = null,
                watchProviders = null,
                credits = null,
                hasReviews = false
            )
    }
}

data class AssociatedContent(
    val backdrops: List<Image>,
    val videos: List<Video>?,
    val externalIds: List<ExternalId>?
) {
    companion object {
        val default: AssociatedContent
            get() = AssociatedContent(
                backdrops = emptyList(),
                videos = null,
                externalIds = null
            )
    }
}