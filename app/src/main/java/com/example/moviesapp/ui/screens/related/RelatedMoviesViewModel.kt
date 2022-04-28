package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.ui.screens.destinations.RelatedMoviesScreenDestination
import com.example.moviesapp.use_case.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.GetRelatedMoviesOfTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RelatedMoviesViewModel @Inject constructor(
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase,
    private val getRelatedMoviesOfTypeUseCase: GetRelatedMoviesOfTypeUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: RelatedMoviesScreenArgs =
        RelatedMoviesScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCase()

    val uiState: StateFlow<RelatedMoviesScreenUiState> =
        deviceLanguage.mapLatest { deviceLanguage ->
            val movies = getRelatedMoviesOfTypeUseCase(
                movieId = navArgs.movieId,
                type = navArgs.type,
                deviceLanguage = deviceLanguage
            ).cachedIn(viewModelScope)

            RelatedMoviesScreenUiState(
                relationType = navArgs.type,
                movies = movies,
                startRoute = navArgs.startRoute
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            RelatedMoviesScreenUiState.getDefault(navArgs.type)
        )
}