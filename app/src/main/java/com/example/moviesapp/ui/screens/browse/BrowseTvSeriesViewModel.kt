package com.example.moviesapp.ui.screens.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.ui.screens.destinations.BrowseTvSeriesScreenDestination
import com.example.moviesapp.use_case.GetTvSeriesOfTypeUseCaseImpl
import com.example.moviesapp.use_case.interfaces.ClearRecentlyBrowsedMoviesUseCase
import com.example.moviesapp.use_case.interfaces.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.interfaces.GetFavouriteTvSeriesCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class BrowseTvSeriesViewModel @Inject constructor(
    private val getDeviceLanguageUseCaseImpl: GetDeviceLanguageUseCase,
    private val getFavouriteTvSeriesCountUseCaseImpl: GetFavouriteTvSeriesCountUseCase,
    private val getTvSeriesOfTypeUseCase: GetTvSeriesOfTypeUseCaseImpl,
    private val clearRecentlyBrowsedTvSeriesUseCase: ClearRecentlyBrowsedMoviesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCaseImpl()

    private val navArgs: BrowseTvSeriesScreenArgs =
        BrowseTvSeriesScreenDestination.argsFrom(savedStateHandle)

    private val favouriteTvSeriesCount: StateFlow<Int> = getFavouriteTvSeriesCountUseCaseImpl()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    val uiState: StateFlow<BrowseTvSeriesScreenUiState> = combine(
        deviceLanguage, favouriteTvSeriesCount
    ) { deviceLanguage, favouriteTvSeriesCount ->
        val tvSeries = getTvSeriesOfTypeUseCase(
            type = navArgs.tvSeriesType,
            deviceLanguage = deviceLanguage
        ).cachedIn(viewModelScope)

        BrowseTvSeriesScreenUiState(
            selectedTvSeriesType = navArgs.tvSeriesType,
            tvSeries = tvSeries,
            favouriteMoviesCount = favouriteTvSeriesCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        BrowseTvSeriesScreenUiState.getDefault(navArgs.tvSeriesType)
    )

    fun onClearClicked() = clearRecentlyBrowsedTvSeriesUseCase()
}