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
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.TvSeriesRepository
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

    private val _seasonDetails: MutableStateFlow<SeasonDetails?> = MutableStateFlow(null)
    val seasonDetails: StateFlow<SeasonDetails?> =
        _seasonDetails.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val episodeCount: StateFlow<Int?> = _seasonDetails.map { details ->
        details?.episodes?.count()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val _episodesStills: MutableStateFlow<Map<Int, List<Image>>> =
        MutableStateFlow(emptyMap())
    val episodeStills: StateFlow<Map<Int, List<Image>>> =
        _episodesStills.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyMap())

    private val _videos: MutableStateFlow<List<Video>?> = MutableStateFlow(null)
    val videos: StateFlow<List<Video>?> = _videos.asStateFlow()

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
                            val seasonDetails = data
                            _seasonDetails.emit(seasonDetails)
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
        if (_episodesStills.value.containsKey(episodeNumber)) {
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
                            episodeStills.collectLatest { current ->
                                val updatedStills = current.toMutableMap().apply {
                                    put(episodeNumber, stills)
                                }
                                _episodesStills.emit(updatedStills)
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
                    val videos = data?.results?.sortedWith(
                        compareBy<Video> { video ->
                            video.language == deviceLanguage.languageCode
                        }.thenByDescending { video ->
                            video.publishedAt
                        }
                    )

                    _videos.emit(videos ?: emptyList())
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