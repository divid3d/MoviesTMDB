package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.ui.screens.destinations.RelatedMoviesScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RelatedMoviesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: RelatedMoviesScreenArgs =
        RelatedMoviesScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    val uiState: StateFlow<RelatedMoviesScreenUiState> = deviceLanguage.map { deviceLanguage ->
        val movies = when (navArgs.type) {
            RelationType.Similar -> {
                movieRepository.similarMovies(
                    movieId = navArgs.movieId,
                    deviceLanguage = deviceLanguage
                )
            }

            RelationType.Recommended -> {
                movieRepository.moviesRecommendations(
                    movieId = navArgs.movieId,
                    deviceLanguage = deviceLanguage
                )
            }
        }

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