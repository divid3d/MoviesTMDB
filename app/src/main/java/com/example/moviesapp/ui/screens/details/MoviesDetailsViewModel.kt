package com.example.moviesapp.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, FlowPreview::class)
@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val configRepository: ConfigRepository,
    private val movieRepository: MovieRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val favouritesMoviesIdsFlow: Flow<List<Int>> =
        favouritesRepository.getFavouritesMoviesIds()

    private val movieId: Flow<Int?> = savedStateHandle.getLiveData<Int>("movieId").asFlow()

    private val _watchAtTime: MutableStateFlow<Date?> = MutableStateFlow(null)
    val watchAtTime: StateFlow<Date?> = _watchAtTime.asStateFlow()

    private val _movieDetails: MutableStateFlow<MovieDetails?> = MutableStateFlow(null)
    private val _credits: MutableStateFlow<Credits?> = MutableStateFlow(null)
    private val _movieBackdrops: MutableStateFlow<List<Image>> = MutableStateFlow(emptyList())
    private val _movieCollection: MutableStateFlow<MovieCollection?> = MutableStateFlow(null)
    private val _watchProviders: MutableStateFlow<WatchProviders?> = MutableStateFlow(null)
    private val _videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)
    private val _hasReviews: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var similarMoviesPagingDataFlow: Flow<PagingData<Movie>>? = null
    var moviesRecommendationPagingDataFlow: Flow<PagingData<Movie>>? = null

    val movieDetails: StateFlow<MovieDetails?> =
        _movieDetails.onEach { movieDetails ->
            movieDetails?.let { details ->
                recentlyBrowsedRepository.addRecentlyBrowsedMovie(details)
            }
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val backdrops: StateFlow<List<Image>> = _movieBackdrops.asStateFlow()

    val isFavourite: StateFlow<Boolean> = combine(
        movieId,
        favouritesMoviesIdsFlow
    ) { id, favouritesIds ->
        id in favouritesIds
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    val movieCollection: StateFlow<MovieCollection?> =
        _movieCollection.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val credits: StateFlow<Credits?> =
        _credits.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val _externalIds: MutableStateFlow<ExternalIds?> = MutableStateFlow(null)
    val externalIds: StateFlow<List<ExternalId>?> =
        _externalIds.filterNotNull().map { externalIds ->
            externalIds.toExternalIdList(type = ExternalContentType.Movie)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val watchProviders: StateFlow<WatchProviders?> = _watchProviders.asStateFlow()

    val videos: StateFlow<List<Video>?> = _videos.asStateFlow()

    val hasReviews: StateFlow<Boolean> = _hasReviews.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    init {
        viewModelScope.launch {
            movieId.collectLatest { movieId ->
                movieId?.let { id ->
                    similarMoviesPagingDataFlow = deviceLanguage.map { deviceLanguage ->
                        movieRepository.similarMovies(movieId = id, deviceLanguage = deviceLanguage)
                    }.flattenMerge().cachedIn(viewModelScope)

                    moviesRecommendationPagingDataFlow = deviceLanguage.map { deviceLanguage ->
                        movieRepository.moviesRecommendations(
                            movieId = movieId,
                            deviceLanguage = deviceLanguage
                        )
                    }.flattenMerge().cachedIn(viewModelScope)

                    deviceLanguage.collectLatest { deviceLanguage ->
                        getMovieInfo(
                            movieId = id,
                            deviceLanguage = deviceLanguage
                        )
                    }
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

    private fun getMovieInfo(movieId: Int, deviceLanguage: DeviceLanguage) {
        getMovieDetails(movieId, deviceLanguage)
        getMovieImages(movieId)
        getExternalIds(movieId)
        getWatchProviders(movieId, deviceLanguage)
        getMovieCredits(movieId, deviceLanguage)
        getMovieReview(movieId)
        getMovieVideos(movieId, deviceLanguage)
    }

    private fun getMovieDetails(movieId: Int, deviceLanguage: DeviceLanguage) {
        movieRepository.movieDetails(
            movieId = movieId,
            isoCode = deviceLanguage.languageCode
        ).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val movieDetails = data
                    _movieDetails.emit(movieDetails)

                    data?.collection?.id?.let { collectionId ->
                        getMovieCollection(
                            collectionId = collectionId,
                            deviceLanguage = deviceLanguage
                        )
                    }
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError()
                firebaseCrashlytics.recordException(exception)
            }
        }
    }

    private fun getMovieCredits(movieId: Int, deviceLanguage: DeviceLanguage) {
        movieRepository.movieCredits(
            movieId = movieId,
            isoCode = deviceLanguage.languageCode
        ).request { response ->
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
                onError()
                firebaseCrashlytics.recordException(exception)
            }
        }
    }

    private fun getMovieImages(movieId: Int) {
        movieRepository.movieImages(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val imagesResponse = data
                    _movieBackdrops.emit(imagesResponse?.backdrops ?: emptyList())
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError()
                firebaseCrashlytics.recordException(exception)
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
                onError()
                firebaseCrashlytics.recordException(exception)
            }
        }
    }

    private fun getMovieCollection(collectionId: Int, deviceLanguage: DeviceLanguage) {
        movieRepository.collection(
            collectionId = collectionId,
            isoCode = deviceLanguage.languageCode
        ).request { response ->
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
                onError()
                firebaseCrashlytics.recordException(exception)
            }
        }
    }

    private fun getWatchProviders(movieId: Int, deviceLanguage: DeviceLanguage) {
        movieRepository.watchProviders(
            movieId = movieId,
        ).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val results = data?.results
                    val watchProviders = results?.getOrElse(deviceLanguage.region) { null }

                    _watchProviders.emit(watchProviders)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError()
                firebaseCrashlytics.recordException(exception)
            }
        }
    }

    private fun getExternalIds(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getExternalIds(
                movieId = movieId
            ).request { response ->
                response.onSuccess {
                    viewModelScope.launch {
                        _externalIds.emit(data)
                    }
                }

                response.onFailure {
                    onError(message)
                }

                response.onException {
                    onError()
                    firebaseCrashlytics.recordException(exception)
                }
            }
        }
    }

    private fun getMovieVideos(movieId: Int, deviceLanguage: DeviceLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getMovieVideos(
                movieId = movieId,
                isoCode = deviceLanguage.languageCode
            ).request { response ->
                response.onSuccess {
                    viewModelScope.launch {
                        val videos = data?.results?.sortedWith(
                            compareBy(
                                { video -> video.official },
                                { video -> video.publishedAt }
                            )
                        )

                        _videos.emit(videos ?: emptyList())
                    }
                }

                response.onFailure {
                    onError(message)
                }

                response.onException {
                    onError()
                    firebaseCrashlytics.recordException(exception)
                }
            }
        }
    }

}