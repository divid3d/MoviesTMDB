package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.ui.screens.destinations.RelatedTvSeriesScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RelatedTvSeriesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: RelatedTvSeriesScreenArgs =
        RelatedTvSeriesScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    val uiState: StateFlow<RelatedTvSeriesScreenUiState> = deviceLanguage.map { deviceLanguage ->
        val tvSeries = when (navArgs.type) {
            RelationType.Similar -> {
                tvSeriesRepository.similarTvSeries(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }

            RelationType.Recommended -> {
                tvSeriesRepository.tvSeriesRecommendations(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }
        }

        RelatedTvSeriesScreenUiState(
            relationType = navArgs.type,
            tvSeries = tvSeries,
            startRoute = navArgs.startRoute
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        RelatedTvSeriesScreenUiState.getDefault(navArgs.type)
    )
}