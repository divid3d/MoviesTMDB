package com.example.moviesapp.ui.screens.seasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.model.Video
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.ui.screens.destinations.SeasonDetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: SeasonDetailsScreenArgs =
        SeasonDetailsScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    private val seasonDetails: MutableStateFlow<SeasonDetails?> = MutableStateFlow(null)

    private val episodesStills: MutableStateFlow<Map<Int, List<Image>>> =
        MutableStateFlow(emptyMap())

    private val videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)

    val uiState: StateFlow<SeasonDetailsScreenUiState> = combine(
        seasonDetails, episodesStills, videos, error
    ) { details, stills, videos, error ->
        SeasonDetailsScreenUiState(
            startRoute = navArgs.startRoute,
            seasonDetails = details,
            videos = videos,
            episodeCount = details?.episodes?.count(),
            episodeStills = stills,
            error = error
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10),
        SeasonDetailsScreenUiState.default
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            deviceLanguage.collectLatest { deviceLanguage ->
                tvSeriesRepository.seasonDetails(
                    tvSeriesId = navArgs.tvSeriesId,
                    seasonNumber = navArgs.seasonNumber,
                    deviceLanguage = deviceLanguage
                ).request { response ->
                    response.onSuccess {
                        viewModelScope.launch {
                            seasonDetails.emit(data)
                        }
                    }

                    response.onFailure {
                        onFailure(this)
                    }

                    response.onException {
                        onError(this)
                    }
                }

                getSeasonVideos(
                    tvSeriesId = navArgs.tvSeriesId,
                    seasonNumber = navArgs.seasonNumber,
                    deviceLanguage = deviceLanguage
                )
            }
        }
    }

    fun getEpisodeStills(episodeNumber: Int) {
        if (episodesStills.value.containsKey(episodeNumber)) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            tvSeriesRepository.episodeImages(
                tvSeriesId = navArgs.tvSeriesId,
                seasonNumber = navArgs.seasonNumber,
                episodeNumber = episodeNumber
            ).request { response ->
                response.onSuccess {
                    viewModelScope.launch {
                        data?.stills?.let { stills ->
                            episodesStills.collectLatest { current ->
                                val updatedStills = current.toMutableMap().apply {
                                    put(episodeNumber, stills)
                                }
                                episodesStills.emit(updatedStills)
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
        }
    }

    private fun getSeasonVideos(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ) {
        tvSeriesRepository.seasonVideos(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber
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