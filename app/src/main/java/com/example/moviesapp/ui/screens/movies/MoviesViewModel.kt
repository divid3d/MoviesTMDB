package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
    private val configRepository: ConfigRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    private val moviesState: StateFlow<MoviesState> = deviceLanguage.map { deviceLanguage ->
        MoviesState(
            discover = movieRepository.discoverMovies(deviceLanguage),
            upcoming = movieRepository.upcomingMovies(deviceLanguage),
            trending = movieRepository.trendingMovies(deviceLanguage),
            topRated = movieRepository.topRatedMovies(deviceLanguage),
            nowPlaying = movieRepository.nowPlayingMovies(deviceLanguage).map { pagingDate ->
                pagingDate.filterCompleteInfo()
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MoviesState.default)

    val uiState: StateFlow<MovieScreenUiState> = moviesState.map { moviesState ->
        MovieScreenUiState(
            moviesState = moviesState,
            favourites = favouritesRepository.favouriteMovies(),
            recentlyBrowsed = recentlyBrowsedRepository.recentlyBrowsedMovies()
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MovieScreenUiState.default)

    private fun PagingData<Movie>.filterCompleteInfo(): PagingData<Movie> {
        return filter { tvSeries ->
            tvSeries.run {
                !backdropPath.isNullOrEmpty() &&
                        !posterPath.isNullOrEmpty() &&
                        title.isNotEmpty() &&
                        overview.isNotEmpty()
            }
        }
    }
}