package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.ui.screens.destinations.RelatedMoviesScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
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

    val movies: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        when (navArgs.type) {
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
    }.flattenMerge().cachedIn(viewModelScope)

}