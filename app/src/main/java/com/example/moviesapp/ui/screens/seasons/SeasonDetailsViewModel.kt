package com.example.moviesapp.ui.screens.seasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val seasonInfo: Flow<SeasonInfo?> =
        savedStateHandle.getLiveData<SeasonInfo>("seasonInfo").asFlow()

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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            seasonInfo.collectLatest { seasonInfo ->
                seasonInfo?.let { info ->
                    tvSeriesRepository.seasonDetails(
                        tvSeriesId = info.tvSeriesId,
                        seasonNumber = info.seasonNumber
                    ).request { response ->
                        response.onSuccess {
                            viewModelScope.launch {
                                val seasonDetails = data
                                _seasonDetails.emit(seasonDetails)
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
        }
    }

    fun getEpisodeStills(episodeNumber: Int) {
        if (_episodesStills.value.containsKey(episodeNumber)) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            seasonInfo.collectLatest { seasonInfo ->
                seasonInfo?.let { info ->
                    tvSeriesRepository.episodeImages(
                        tvSeriesId = info.tvSeriesId,
                        seasonNumber = info.seasonNumber,
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
                                onError(message)
                            }

                            response.onException {
                                onError(message)
                            }
                        }
                    }
                }
            }
        }
    }

}