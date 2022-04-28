package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.ui.screens.destinations.RelatedTvSeriesScreenDestination
import com.example.moviesapp.use_case.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.GetRelatedTvSeriesOfTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RelatedTvSeriesViewModel @Inject constructor(
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase,
    private val getRelatedTvSeriesOfTypeUseCase: GetRelatedTvSeriesOfTypeUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: RelatedTvSeriesScreenArgs =
        RelatedTvSeriesScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCase()

    val uiState: StateFlow<RelatedTvSeriesScreenUiState> =
        deviceLanguage.mapLatest { deviceLanguage ->
            val tvSeries = getRelatedTvSeriesOfTypeUseCase(
                tvSeriesId = navArgs.tvSeriesId,
                type = navArgs.type,
                deviceLanguage = deviceLanguage
            ).cachedIn(viewModelScope)

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