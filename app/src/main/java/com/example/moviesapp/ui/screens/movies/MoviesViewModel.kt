package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), MoviesState.default)

    val uiState: StateFlow<MovieScreenUiState> = moviesState.map { moviesState ->
        MovieScreenUiState(
            moviesState = moviesState,
            favourites = favouritesRepository.favouriteMovies(),
            recentlyBrowsed = recentlyBrowsedRepository.recentlyBrowsedMovies()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), MovieScreenUiState.default)

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