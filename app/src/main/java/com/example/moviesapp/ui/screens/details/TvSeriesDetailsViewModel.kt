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
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: TvSeriesDetailsScreenArgs =
        TvSeriesDetailsScreenDestination.argsFrom(savedStateHandle)

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val favouriteTvSeriesIds: Flow<List<Int>> =
        favouritesRepository.getFavouriteTvSeriesIds()

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)
    private val tvSeriesDetails: StateFlow<TvSeriesDetails?> =
        _tvSeriesDetails.onEach { tvSeriesDetails ->
            tvSeriesDetails?.let { details ->
                recentlyBrowsedRepository.addRecentlyBrowsedTvSeries(details)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val tvSeriesBackdrops: MutableStateFlow<List<Image>> = MutableStateFlow(emptyList())
    private val videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)
    private val nextEpisodeDaysRemaining: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val watchProviders: MutableStateFlow<WatchProviders?> = MutableStateFlow(null)
    private val reviewsCount: MutableStateFlow<Int> = MutableStateFlow(0)

    private val isFavourite: StateFlow<Boolean> = favouriteTvSeriesIds.map { favouriteIds ->
        navArgs.tvSeriesId in favouriteIds
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    private val _externalIds: MutableStateFlow<ExternalIds?> = MutableStateFlow(null)
    val externalIds: StateFlow<List<ExternalId>?> =
        _externalIds.filterNotNull().map { externalIds ->
            externalIds.toExternalIdList(type = ExternalContentType.Tv)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val additionalInfo: StateFlow<AdditionalTvSeriesDetailsInfo> = combine(
        isFavourite, nextEpisodeDaysRemaining, watchProviders, reviewsCount
    ) { isFavourite, nextEpisodeDaysRemaining, watchProviders, reviewsCount ->
        AdditionalTvSeriesDetailsInfo(
            isFavourite = isFavourite,
            nextEpisodeRemainingDays = nextEpisodeDaysRemaining,
            watchProviders = watchProviders,
            reviewsCount = reviewsCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10),
        AdditionalTvSeriesDetailsInfo.default
    )

    private val associatedTvSeries: StateFlow<AssociatedTvSeries> =
        deviceLanguage.map { deviceLanguage ->
            AssociatedTvSeries(
                similar = tvSeriesRepository.similarTvSeries(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                ),
                recommendations = tvSeriesRepository.tvSeriesRecommendations(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), AssociatedTvSeries.default)

    private val associatedContent: StateFlow<AssociatedContent> = combine(
        tvSeriesBackdrops, videos, externalIds
    ) { backdrops, videos, externalIds ->
        AssociatedContent(
            backdrops = backdrops,
            videos = videos,
            externalIds = externalIds
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), AssociatedContent.default)

    val uiState: StateFlow<TvSeriesDetailsScreenUiState> = combine(
        tvSeriesDetails, additionalInfo, associatedTvSeries, associatedContent, error
    ) { details, additionalInfo, associatedTvSeries, visualContent, error ->
        TvSeriesDetailsScreenUiState(
            startRoute = navArgs.startRoute,
            tvSeriesDetails = details,
            additionalTvSeriesDetailsInfo = additionalInfo,
            associatedTvSeries = associatedTvSeries,
            associatedContent = visualContent,
            error = error
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10),
        TvSeriesDetailsScreenUiState.getDefault(navArgs.startRoute)
    )

    init {
        viewModelScope.launch {
            deviceLanguage.collectLatest { deviceLanguage ->
                getTvSeriesInfo(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }
        }
    }

    fun onLikeClick(tvSeriesDetails: TvSeriesDetails) {
        favouritesRepository.likeTvSeries(tvSeriesDetails)
    }

    fun onUnlikeClick(tvSeriesDetails: TvSeriesDetails) {
        favouritesRepository.unlikeTvSeries(tvSeriesDetails)
    }

    private fun getTvSeriesInfo(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        getTvSeriesDetails(tvSeriesId, deviceLanguage)
        getMovieImages(tvSeriesId)
        getExternalIds(tvSeriesId)
        getWatchProviders(tvSeriesId, deviceLanguage)
        getTvSeriesVideos(tvSeriesId, deviceLanguage)
        getTvSeriesReview(tvSeriesId)
    }

    private fun getTvSeriesDetails(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        tvSeriesRepository.getTvSeriesDetails(
            tvSeriesId = tvSeriesId,
            deviceLanguage = deviceLanguage
        ).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val tvSeriesDetails = data
                    _tvSeriesDetails.emit(tvSeriesDetails)

                    tvSeriesDetails?.nextEpisodeToAir?.airDate?.let { date ->
                        getNextEpisodeDaysRemaining(date)
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

    private fun getMovieImages(tvSeriesId: Int) {
        tvSeriesRepository.tvSeriesImages(tvSeriesId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    tvSeriesBackdrops.emit(data?.backdrops ?: emptyList())
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

    private fun getTvSeriesReview(tvSeriesId: Int) {
        tvSeriesRepository.tvSeriesReview(tvSeriesId).request { response ->
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

    private fun getWatchProviders(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        tvSeriesRepository.watchProviders(
            tvSeriesId = tvSeriesId
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

    private fun getExternalIds(tvSeriesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tvSeriesRepository.getExternalIds(
                tvSeriesId = tvSeriesId
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

    private fun getTvSeriesVideos(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            tvSeriesRepository.tvSeriesVideos(
                tvSeriesId = tvSeriesId,
                isoCode = deviceLanguage.languageCode
            ).request { response ->
                response.onSuccess {
                    viewModelScope.launch {
                        val data = data?.results?.sortedWith(
                            compareBy(
                                { video -> video.official },
                                { video -> video.publishedAt }
                            )
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

    private suspend fun getNextEpisodeDaysRemaining(nextEpisodeAirDate: Date) {
        val millionSeconds = nextEpisodeAirDate.time - Calendar.getInstance().timeInMillis
        val daysDiff = TimeUnit.MILLISECONDS.toDays(millionSeconds)

        nextEpisodeDaysRemaining.emit(if (daysDiff < 0) 0 else daysDiff)
    }

}