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
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
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

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val favouriteTvSeriesIds: Flow<List<Int>> =
        favouritesRepository.getFavouriteTvSeriesIds()

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)

    private val tvSeriesId: Flow<Int> = savedStateHandle.getLiveData<Int>("tvSeriesId").asFlow()

    val similarTvSeries: Flow<PagingData<TvSeries>>? = combine(
        tvSeriesId, deviceLanguage
    ) { id, deviceLanguage ->
        tvSeriesRepository.similarTvSeries(
            tvSeriesId = id,
            deviceLanguage = deviceLanguage
        )
    }.flattenMerge().cachedIn(viewModelScope)

    val tvSeriesRecommendations: Flow<PagingData<TvSeries>>? = combine(
        tvSeriesId, deviceLanguage
    ) { id, deviceLanguage ->
        tvSeriesRepository.tvSeriesRecommendations(
            tvSeriesId = id,
            deviceLanguage = deviceLanguage
        )
    }.flattenMerge().cachedIn(viewModelScope)

    private val _tvSeriesBackdrops: MutableStateFlow<List<Image>> = MutableStateFlow(emptyList())
    private val _videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)
    private val _nextEpisodeDaysRemaining: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val _watchProviders: MutableStateFlow<WatchProviders?> = MutableStateFlow(null)
    private val _hasReviews: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val tvSeriesDetails: StateFlow<TvSeriesDetails?> =
        _tvSeriesDetails
            .onEach { tvSeriesDetails ->
                tvSeriesDetails?.let { details ->
                    recentlyBrowsedRepository.addRecentlyBrowsedTvSeries(details)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val backdrops: StateFlow<List<Image>> = _tvSeriesBackdrops.asStateFlow()

    val isFavourite: StateFlow<Boolean> = combine(
        tvSeriesId, favouriteTvSeriesIds
    ) { id, favouritesId ->
        id in favouritesId
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    val nextEpisodeDaysRemaining: StateFlow<Long?> = _nextEpisodeDaysRemaining.asStateFlow()
    val watchProviders: StateFlow<WatchProviders?> = _watchProviders.asStateFlow()
    val videos: StateFlow<List<Video>?> = _videos.asStateFlow()

    private val _externalIds: MutableStateFlow<ExternalIds?> = MutableStateFlow(null)
    val externalIds: StateFlow<List<ExternalId>?> =
        _externalIds.filterNotNull().map { externalIds ->
            externalIds.toExternalIdList(type = ExternalContentType.Tv)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val hasReviews: StateFlow<Boolean> = _hasReviews.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    init {
        viewModelScope.launch {
            tvSeriesId.collectLatest { tvSeriesId ->
                deviceLanguage.collectLatest { deviceLanguage ->
                    getTvSeriesInfo(
                        tvSeriesId = tvSeriesId,
                        deviceLanguage = deviceLanguage
                    )
                }
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
                onError(message)
            }

            response.onException {
                onError()
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }
    }

    private fun getMovieImages(tvSeriesId: Int) {
        tvSeriesRepository.tvSeriesImages(tvSeriesId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val imagesResponse = data
                    _tvSeriesBackdrops.emit(imagesResponse?.backdrops ?: emptyList())
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError()
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }
    }

    private fun getTvSeriesReview(tvSeriesId: Int) {
        tvSeriesRepository.tvSeriesReview(tvSeriesId).request { response ->
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
                FirebaseCrashlytics.getInstance().recordException(exception)
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
                    val watchProviders = results?.getOrElse(deviceLanguage.region) { null }

                    _watchProviders.emit(watchProviders)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError()
                FirebaseCrashlytics.getInstance().recordException(exception)
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
                    onError(message)
                }

                response.onException {
                    onError()
                    FirebaseCrashlytics.getInstance().recordException(exception)
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
                    FirebaseCrashlytics.getInstance().recordException(exception)
                }
            }
        }
    }

    private suspend fun getNextEpisodeDaysRemaining(nextEpisodeAirDate: Date) {
        val millionSeconds = nextEpisodeAirDate.time - Calendar.getInstance().timeInMillis
        val daysDiff = TimeUnit.MILLISECONDS.toDays(millionSeconds)

        _nextEpisodeDaysRemaining.emit(if (daysDiff < 0) 0 else daysDiff)
    }

}