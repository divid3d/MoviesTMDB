package com.example.moviesapp.ui.screens.seasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.model.*
import com.example.moviesapp.ui.screens.destinations.SeasonDetailsScreenDestination
import com.example.moviesapp.use_case.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getDeviceLanguageUseCaseImpl: GetDeviceLanguageUseCase,
    private val getSeasonDetailsUseCase: GetSeasonDetailsUseCase,
    private val getSeasonsVideosUseCaseImpl: GetSeasonsVideosUseCase,
    private val getSeasonCreditsUseCase: GetSeasonCreditsUseCase,
    private val getEpisodeStillsUseCaseImpl: GetEpisodeStillsUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: SeasonDetailsScreenArgs =
        SeasonDetailsScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCaseImpl()

    private val seasonDetails: MutableStateFlow<SeasonDetails?> = MutableStateFlow(null)
    private val aggregatedCredits: MutableStateFlow<AggregatedCredits?> = MutableStateFlow(null)
    private val episodesStills: MutableStateFlow<Map<Int, List<Image>>> =
        MutableStateFlow(emptyMap())

    private val videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)

    val uiState: StateFlow<SeasonDetailsScreenUiState> = combine(
        seasonDetails, aggregatedCredits, episodesStills, videos, error
    ) { details, credits, stills, videos, error ->
        SeasonDetailsScreenUiState(
            startRoute = navArgs.startRoute,
            seasonDetails = details,
            aggregatedCredits = credits,
            videos = videos,
            episodeCount = details?.episodes?.count(),
            episodeStills = stills,
            error = error
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SeasonDetailsScreenUiState.default
    )

    init {
        viewModelScope.launch(defaultDispatcher) {
            supervisorScope {
                deviceLanguage.collectLatest { deviceLanguage ->
                    launch {
                        getSeasonDetailsUseCase(
                            tvSeriesId = navArgs.tvSeriesId,
                            seasonNumber = navArgs.seasonNumber,
                            deviceLanguage = deviceLanguage
                        ).onSuccess {
                            viewModelScope.launch {
                                seasonDetails.emit(data)
                            }
                        }.onFailure {
                            onFailure(this)
                        }.onException {
                            onError(this)
                        }
                    }

                    launch {
                        getSeasonCredits(
                            tvSeriesId = navArgs.tvSeriesId,
                            seasonNumber = navArgs.seasonNumber,
                            deviceLanguage = deviceLanguage
                        )
                    }

                    launch {
                        getSeasonVideos(
                            tvSeriesId = navArgs.tvSeriesId,
                            seasonNumber = navArgs.seasonNumber,
                            deviceLanguage = deviceLanguage
                        )
                    }
                }
            }
        }
    }

    fun getEpisodeStills(episodeNumber: Int) {
        if (episodesStills.value.containsKey(episodeNumber)) {
            return
        }

        viewModelScope.launch(defaultDispatcher) {
            getEpisodeStillsUseCaseImpl(
                tvSeriesId = navArgs.tvSeriesId,
                seasonNumber = navArgs.seasonNumber,
                episodeNumber = episodeNumber
            ).onSuccess {
                viewModelScope.launch(defaultDispatcher) {
                    episodesStills.collectLatest { current ->
                        val updatedStills = current.toMutableMap().apply {
                            put(episodeNumber, data ?: emptyList())
                        }
                        episodesStills.emit(updatedStills)
                    }
                }
            }.onFailure {
                onFailure(this)
            }.onException {
                onError(this)
            }
        }
    }

    private suspend fun getSeasonCredits(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ) {
        getSeasonCreditsUseCase(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            deviceLanguage = deviceLanguage
        ).onSuccess {
            data?.let { credits ->
                viewModelScope.launch {
                    aggregatedCredits.emit(credits)
                }
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }

    private suspend fun getSeasonVideos(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ) {
        getSeasonsVideosUseCaseImpl(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
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