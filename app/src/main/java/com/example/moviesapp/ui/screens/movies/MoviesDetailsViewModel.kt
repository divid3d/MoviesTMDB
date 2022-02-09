package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.other.getImageUrl
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val movieRepository: MovieRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val config: StateFlow<Config?> = configRepository.config
    private val favouritesMoviesIdsFlow: Flow<List<Int>> =
        favouritesRepository.getFavouritesMoviesIds()

    private val movieId: Flow<Int?> = savedStateHandle.getLiveData<Int>("movieId").asFlow()

    private val _watchAtTime: MutableStateFlow<Date?> = MutableStateFlow(null)
    val watchAtTime: StateFlow<Date?> = _watchAtTime.asStateFlow()

    private val _movieDetails: MutableStateFlow<MovieDetails?> = MutableStateFlow(null)
    private val _credits: MutableStateFlow<Credits?> = MutableStateFlow(null)
    private val _movieBackdrops: MutableStateFlow<List<Image>?> = MutableStateFlow(null)
    private val _movieCollection: MutableStateFlow<MovieCollection?> = MutableStateFlow(null)
    private val _hasReviews: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var similarMoviesPagingDataFlow: Flow<PagingData<Presentable>>? = null
    var moviesRecommendationPagingDataFlow: Flow<PagingData<Presentable>>? = null

    val movieDetails: StateFlow<MovieDetails?> = combine(
        _movieDetails, config
    ) { movieDetails, config ->
        val posterUrl = config?.getImageUrl(movieDetails?.posterPath)
        val backdropUrl = config?.getImageUrl(movieDetails?.backdropPath)

        movieDetails?.copy(
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
        )
    }
        .onEach { movieDetails ->
            movieDetails?.let { details ->
                recentlyBrowsedRepository.addRecentlyBrowsedMovie(details)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val backdrops: StateFlow<List<Image>> = combine(
        _movieBackdrops, config
    ) { backdrops, config ->
        backdrops?.map { backdrop -> backdrop.appendUrls(config) } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    val isFavourite: StateFlow<Boolean> = combine(
        movieId,
        favouritesMoviesIdsFlow
    ) { id, favouritesIds ->
        id in favouritesIds
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    val movieCollection: StateFlow<MovieCollection?> = combine(
        _movieCollection, config
    ) { collection, config ->
        collection?.copy(
            parts = collection.parts.map { part -> part.appendUrls(config) }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val credits: StateFlow<Credits?> = combine(
        _credits, config
    ) { credits, config ->
        val cast = credits?.cast?.map { member ->
            val profileUrl = config?.getImageUrl(member.profilePath, size = "w185")

            member.copy(profileUrl = profileUrl)
        }

        val crew = credits?.crew?.map { member ->
            val profileUrl = config?.getImageUrl(member.profilePath, size = "w185")

            member.copy(profileUrl = profileUrl)
        }

        credits?.copy(
            cast = cast,
            crew = crew
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)


    val hasReviews: StateFlow<Boolean> = _hasReviews.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    init {
        viewModelScope.launch {
            movieId.collectLatest { movieId ->
                movieId?.let { id ->
                    similarMoviesPagingDataFlow = movieRepository.similarMovies(id)
                        .cachedIn(viewModelScope)
                        .combine(config) { moviePagingData, config ->
                            moviePagingData
                                .filter { movie -> movie.id != movieId }
                                .map { movie -> movie.appendUrls(config) }
                        }

                    moviesRecommendationPagingDataFlow =
                        movieRepository.moviesRecommendations(movieId)
                            .cachedIn(viewModelScope)
                            .combine(config) { moviePagingData, config ->
                                moviePagingData
                                    .filter { movie -> movie.id != movieId }
                                    .map { movie -> movie.appendUrls(config) }
                            }

                    getMovieInfo(id)
                }
            }
        }

        startRefreshingWatchAtTime()
    }

    private fun startRefreshingWatchAtTime() {
        viewModelScope.launch {
            _movieDetails.collectLatest { details ->
                while (isActive) {
                    details?.runtime?.let { runtime ->
                        if (runtime > 0) {
                            runtime.minutes.toComponents { hours, minutes, _, _ ->
                                val watchAtTime = Calendar.getInstance().apply {
                                    time = Date()

                                    add(Calendar.HOUR, hours.toInt())
                                    add(Calendar.MINUTE, minutes)
                                }.time

                                _watchAtTime.emit(watchAtTime)
                            }
                        }
                    }

                    delay(10.seconds)
                }
            }
        }
    }

    fun onLikeClick(movieDetails: MovieDetails) {
        favouritesRepository.likeMovie(movieDetails)
    }

    fun onUnlikeClick(movieDetails: MovieDetails) {
        favouritesRepository.unlikeMovie(movieDetails)
    }

    private fun getMovieInfo(movieId: Int) {
        getMovieDetails(movieId)
        getMovieImages(movieId)
        getMovieCredits(movieId)
        getMovieReview(movieId)
    }

    private fun getMovieDetails(movieId: Int) {
        movieRepository.movieDetails(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val movieDetails = data
                    _movieDetails.emit(movieDetails)

                    data?.collection?.id?.let { collectionId ->
                        getMovieCollection(collectionId)
                    }
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

    private fun getMovieCredits(movieId: Int) {
        movieRepository.movieCredits(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val credits = data
                    _credits.emit(credits)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

    private fun getMovieImages(movieId: Int) {
        movieRepository.movieImages(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val imagesResponse = data
                    _movieBackdrops.emit(imagesResponse?.backdrops)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

    private fun getMovieReview(movieId: Int) {
        movieRepository.movieReview(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val hasReviews = (data?.totalResults ?: 0) > 1

                    _hasReviews.emit(hasReviews)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

    private fun getMovieCollection(collectionId: Int) {
        movieRepository.collection(collectionId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val collectionResponse = data

                    collectionResponse?.let { response ->
                        val name = response.name
                        val parts = response.parts

                        val movieCollection = MovieCollection(
                            name = name,
                            parts = parts
                        )

                        _movieCollection.emit(movieCollection)
                    }
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }
}