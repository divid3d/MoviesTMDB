package com.example.moviesapp.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val movieRepository: MovieRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: MovieDetailsScreenArgs =
        MovieDetailsScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val favouritesMoviesIdsFlow: Flow<List<Int>> =
        favouritesRepository.getFavouritesMoviesIds()

    private val watchAtTime: MutableStateFlow<Date?> = MutableStateFlow(null)
    private val _movieDetails: MutableStateFlow<MovieDetails?> = MutableStateFlow(null)
    private val movieDetails: StateFlow<MovieDetails?> =
        _movieDetails.onEach { movieDetails ->
            movieDetails?.let { details ->
                recentlyBrowsedRepository.addRecentlyBrowsedMovie(details)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val credits: MutableStateFlow<Credits?> = MutableStateFlow(null)
    private val movieBackdrops: MutableStateFlow<List<Image>> = MutableStateFlow(emptyList())
    private val movieCollection: MutableStateFlow<MovieCollection?> = MutableStateFlow(null)
    private val watchProviders: MutableStateFlow<WatchProviders?> = MutableStateFlow(null)
    private val videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)
    private val reviewsCount: MutableStateFlow<Int> = MutableStateFlow(0)

    private val isFavourite: Flow<Boolean> = favouritesMoviesIdsFlow.mapLatest { favouriteIds ->
        navArgs.movieId in favouriteIds
    }

    private val _externalIds: MutableStateFlow<ExternalIds?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val externalIds: StateFlow<List<ExternalId>?> =
        _externalIds.filterNotNull().mapLatest { externalIds ->
            externalIds.toExternalIdList(type = ExternalContentType.Movie)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val additionalInfo: StateFlow<AdditionalMovieDetailsInfo> = combine(
        isFavourite, watchAtTime, watchProviders, credits, reviewsCount
    ) { isFavourite, watchAtTime, watchProviders, credits, reviewsCount ->
        AdditionalMovieDetailsInfo(
            isFavourite = isFavourite,
            watchAtTime = watchAtTime,
            watchProviders = watchProviders,
            credits = credits,
            reviewsCount = reviewsCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10),
        AdditionalMovieDetailsInfo.default
    )

    private val associatedMovies: StateFlow<AssociatedMovies> = combine(
        deviceLanguage, movieCollection, credits
    ) { deviceLanguage, collection, credits ->
        val directors = credits?.crew?.filter { member -> member.job == "Director" }
        val mainDirector = if (directors?.count() == 1) directors.first() else null

        AssociatedMovies(
            collection = collection,
            similar = movieRepository.similarMovies(
                movieId = navArgs.movieId,
                deviceLanguage = deviceLanguage
            ),
            recommendations = movieRepository.moviesRecommendations(
                movieId = navArgs.movieId,
                deviceLanguage = deviceLanguage
            ),
            directorMovies = if (mainDirector != null) {
                DirectorMovies(
                    directorName = mainDirector.name,
                    movies = movieRepository.moviesOfDirector(
                        directorId = mainDirector.id,
                        deviceLanguage = deviceLanguage
                    )
                )
            } else DirectorMovies.default
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), AssociatedMovies.default)

    private val associatedContent: StateFlow<AssociatedContent> = combine(
        movieBackdrops, videos, externalIds
    ) { backdrops, videos, externalIds ->
        AssociatedContent(
            backdrops = backdrops,
            videos = videos,
            externalIds = externalIds
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), AssociatedContent.default)

    val uiState: StateFlow<MovieDetailsScreenUiState> = combine(
        movieDetails, additionalInfo, associatedMovies, associatedContent, error
    ) { details, additionalInfo, associatedMovies, visualContent, error ->
        MovieDetailsScreenUiState(
            startRoute = navArgs.startRoute,
            movieDetails = details,
            additionalMovieDetailsInfo = additionalInfo,
            associatedMovies = associatedMovies,
            associatedContent = visualContent,
            error = error
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        MovieDetailsScreenUiState.getDefault(navArgs.startRoute)
    )


    init {
        viewModelScope.launch {
            deviceLanguage.collectLatest { deviceLanguage ->
                getMovieInfo(
                    movieId = navArgs.movieId,
                    deviceLanguage = deviceLanguage
                )
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
                                val time = Calendar.getInstance().apply {
                                    time = Date()

                                    add(Calendar.HOUR, hours.toInt())
                                    add(Calendar.MINUTE, minutes)
                                }.time

                                watchAtTime.emit(time)
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
                onFailure(this)
            }

            response.onException {
                onError(this)
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
                    credits.emit(data)
                }
            }

            response.onFailure {
                onFailure(this)
            }

            response.onException {
                onError(this)
            }
        }
    }

    private fun getMovieImages(movieId: Int) {
        movieRepository.movieImages(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    movieBackdrops.emit(data?.backdrops ?: emptyList())
                }
            }

            response.onFailure {
                onFailure(this)
            }

            response.onException {
                onError(this)
            }
        }
    }

    private fun getMovieReview(movieId: Int) {
        movieRepository.movieReview(movieId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    reviewsCount.emit(data?.totalResults ?: 0)
                }
            }

            response.onFailure {
                onFailure(this)
            }

            response.onException {
                onError(this)
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

                        val collection = MovieCollection(
                            name = name,
                            parts = parts
                        )

                        movieCollection.emit(collection)
                    }
                }
            }

            response.onFailure {
                onFailure(this)
            }

            response.onException {
                onError(this)
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
                    val providers = results?.getOrElse(deviceLanguage.region) { null }

                    watchProviders.emit(providers)
                }
            }

            response.onFailure {
                onFailure(this)
            }

            response.onException {
                onError(this)
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
                    onFailure(this)
                }

                response.onException {
                    onError(this)
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
                        val data = data?.results?.sortedWith(
                            compareBy<Video> { video ->
                                video.language == deviceLanguage.languageCode
                            }.thenByDescending { video ->
                                video.publishedAt
                            }
                        )

                        videos.emit(data ?: emptyList())
                    }
                }

                response.onFailure {
                    onFailure(this)
                }

                response.onException {
                    onError(this)
                }
            }
        }
    }

}