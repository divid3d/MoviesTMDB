package com.example.moviesapp.ui.screens.seasons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.SeasonDetails
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val config: StateFlow<Config?> = configRepository.config

    private val seasonInfo: Flow<SeasonInfo?> =
        savedStateHandle.getLiveData<SeasonInfo>("seasonInfo").asFlow()

    private val _seasonDetails: MutableStateFlow<SeasonDetails?> = MutableStateFlow(null)
    val seasonDetails: StateFlow<SeasonDetails?> = combine(
        _seasonDetails, config
    ) { details, config ->
        details?.appendUrls(config)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val episodeCount: StateFlow<Int?> = _seasonDetails.map { details ->
        details?.episodes?.count()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    init {
        viewModelScope.launch {
            seasonInfo.collectLatest { seasonInfo ->
                seasonInfo?.let { info ->
                    movieRepository.seasonDetails(
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

}