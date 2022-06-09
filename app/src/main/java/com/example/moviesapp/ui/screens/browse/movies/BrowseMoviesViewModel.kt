package com.example.moviesapp.ui.screens.browse.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.ui.screens.destinations.BrowseMoviesScreenDestination
import com.example.moviesapp.use_case.interfaces.ClearRecentlyBrowsedMoviesUseCase
import com.example.moviesapp.use_case.interfaces.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.interfaces.GetFavouritesMovieCountUseCase
import com.example.moviesapp.use_case.interfaces.GetMoviesOfTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class BrowseMoviesViewModel @Inject constructor(
    private val getDeviceLanguageUseCaseImpl: GetDeviceLanguageUseCase,
    private val getMoviesOfTypeUseCase: GetMoviesOfTypeUseCase,
    private val getFavouritesMoviesCountUseCase: GetFavouritesMovieCountUseCase,
    private val getClearRecentlyBrowsedMoviesUseCaseImpl: ClearRecentlyBrowsedMoviesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCaseImpl()

    private val navArgs: BrowseMoviesScreenArgs =
        BrowseMoviesScreenDestination.argsFrom(savedStateHandle)

    private val favouriteMoviesCount: StateFlow<Int> = getFavouritesMoviesCountUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    val uiState: StateFlow<BrowseMoviesScreenUiState> = combine(
        deviceLanguage, favouriteMoviesCount
    ) { deviceLanguage, favouriteMoviesCount ->
        val movies = getMoviesOfTypeUseCase(
            type = navArgs.movieType,
            deviceLanguage = deviceLanguage
        ).cachedIn(viewModelScope)

        BrowseMoviesScreenUiState(
            selectedMovieType = navArgs.movieType,
            movies = movies,
            favouriteMoviesCount = favouriteMoviesCount
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        BrowseMoviesScreenUiState.getDefault(navArgs.movieType)
    )

    fun onClearClicked() = getClearRecentlyBrowsedMoviesUseCaseImpl()
}