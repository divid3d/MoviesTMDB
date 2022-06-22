package com.example.moviesapp.ui.screens.details.tvseries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.model.*
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.details.movie.AssociatedContent
import com.example.moviesapp.use_case.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase,
    private val addRecentlyBrowsedTvSeriesUseCase: AddRecentlyBrowsedTvSeriesUseCase,
    private val getFavouriteTvSeriesIdsUseCase: GetFavouriteTvSeriesIdsUseCase,
    private val getRelatedTvSeriesOfTypeUseCase: GetRelatedTvSeriesOfTypeUseCase,
    private val getTvSeriesDetailsUseCase: GetTvSeriesDetailsUseCase,
    private val getNextEpisodeDaysRemainingUseCase: GetNextEpisodeDaysRemainingUseCase,
    private val getTvSeriesExternalIdsUseCase: GetTvSeriesExternalIdsUseCase,
    private val getTvSeriesImagesUseCase: GetTvSeriesImagesUseCase,
    private val getTvSeriesReviewsCountUseCase: GetTvSeriesReviewsCountUseCase,
    private val getTvSeriesVideosUseCase: GetTvSeriesVideosUseCase,
    private val getTvSeriesWatchProvidersUseCase: GetTvSeriesWatchProvidersUseCase,
    private val likeTvSeriesUseCase: LikeTvSeriesUseCase,
    private val unlikeTvSeriesUseCase: UnlikeTvSeriesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: TvSeriesDetailsScreenArgs =
        TvSeriesDetailsScreenDestination.argsFrom(savedStateHandle)

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCase()
    private val favouriteTvSeriesIds: Flow<List<Int>> = getFavouriteTvSeriesIdsUseCase()

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)
    private val tvSeriesDetails: StateFlow<TvSeriesDetails?> =
        _tvSeriesDetails.onEach { tvSeriesDetails ->
            tvSeriesDetails?.let(addRecentlyBrowsedTvSeriesUseCase::invoke)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val tvSeriesBackdrops: MutableStateFlow<List<Image>> = MutableStateFlow(emptyList())
    private val videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)
    private val nextEpisodeDaysRemaining: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val watchProviders: MutableStateFlow<WatchProviders?> = MutableStateFlow(null)
    private val reviewsCount: MutableStateFlow<Int> = MutableStateFlow(0)

    private val isFavourite: StateFlow<Boolean> = favouriteTvSeriesIds.mapLatest { favouriteIds ->
        navArgs.tvSeriesId in favouriteIds
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    private val externalIds: MutableStateFlow<List<ExternalId>?> = MutableStateFlow(null)

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
        deviceLanguage.mapLatest { deviceLanguage ->
            AssociatedTvSeries(
                similar = getRelatedTvSeriesOfTypeUseCase(
                    tvSeriesId = navArgs.tvSeriesId,
                    type = RelationType.Similar,
                    deviceLanguage = deviceLanguage
                ).cachedIn(viewModelScope),
                recommendations = getRelatedTvSeriesOfTypeUseCase(
                    tvSeriesId = navArgs.tvSeriesId,
                    type = RelationType.Recommended,
                    deviceLanguage = deviceLanguage
                ).cachedIn(viewModelScope)
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
        SharingStarted.Eagerly,
        TvSeriesDetailsScreenUiState.getDefault(navArgs.startRoute)
    )

    init {
        getTvSeriesInfo()
    }

    fun onLikeClick(tvSeriesDetails: TvSeriesDetails) {
        likeTvSeriesUseCase(tvSeriesDetails)
    }

    fun onUnlikeClick(tvSeriesDetails: TvSeriesDetails) {
        unlikeTvSeriesUseCase(tvSeriesDetails)
    }

    private fun getTvSeriesInfo() {
        val tvSeriesId = navArgs.tvSeriesId

        viewModelScope.launch(defaultDispatcher) {
            supervisorScope {
                launch {
                    getTvSeriesImages(tvSeriesId)
                }

                launch {
                    getExternalIds(tvSeriesId)
                }

                launch {
                    getTvSeriesReviewsCount(tvSeriesId)
                }

                deviceLanguage.collectLatest { deviceLanguage ->
                    launch {
                        getTvSeriesDetails(
                            tvSeriesId = tvSeriesId,
                            deviceLanguage = deviceLanguage
                        )
                    }

                    launch {
                        getWatchProviders(
                            tvSeriesId = tvSeriesId,
                            deviceLanguage = deviceLanguage
                        )
                    }

                    launch {
                        getTvSeriesVideos(
                            tvSeriesId = tvSeriesId,
                            deviceLanguage = deviceLanguage
                        )
                    }
                }
            }
        }
    }

    private suspend fun getTvSeriesDetails(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        getTvSeriesDetailsUseCase(
            tvSeriesId = tvSeriesId,
            deviceLanguage = deviceLanguage
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                val tvSeriesDetails = data
                _tvSeriesDetails.emit(tvSeriesDetails)

                tvSeriesDetails?.nextEpisodeToAir?.airDate?.let { date ->
                    val daysRemaining = getNextEpisodeDaysRemainingUseCase(date)
                    nextEpisodeDaysRemaining.emit(daysRemaining)
                }
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }

    private suspend fun getTvSeriesImages(tvSeriesId: Int) {
        getTvSeriesImagesUseCase(tvSeriesId)
            .onSuccess {
                viewModelScope.launch(defaultDispatcher) {
                    tvSeriesBackdrops.emit(data ?: emptyList())
                }
            }.onFailure {
                onFailure(this)
            }.onException {
                onError(this)
            }
    }

    private suspend fun getTvSeriesReviewsCount(tvSeriesId: Int) {
        getTvSeriesReviewsCountUseCase(tvSeriesId)
            .onSuccess {
                viewModelScope.launch(defaultDispatcher) {
                    reviewsCount.emit(data ?: 0)
                }
            }.onFailure {
                onFailure(this)
            }.onException {
                onError(this)
            }
    }

    private suspend fun getWatchProviders(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        getTvSeriesWatchProvidersUseCase(
            tvSeriesId = tvSeriesId,
            deviceLanguage = deviceLanguage
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                watchProviders.emit(data)
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }

    private suspend fun getExternalIds(tvSeriesId: Int) {
        getTvSeriesExternalIdsUseCase(
            tvSeriesId = tvSeriesId
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                externalIds.emit(data)
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }

    private suspend fun getTvSeriesVideos(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        getTvSeriesVideosUseCase(
            tvSeriesId = tvSeriesId,
            deviceLanguage = deviceLanguage
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                videos.emit(data ?: emptyList())
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }
}