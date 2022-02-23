package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.RecentlyBrowsedMovie
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

    private val discover = deviceLanguage.map { deviceLanguage ->
        movieRepository.discoverMovies(deviceLanguage)
    }

    private val upcoming = deviceLanguage.map { deviceLanguage ->
        movieRepository.upcomingMovies(deviceLanguage)
    }

    private val trending = deviceLanguage.map { deviceLanguage ->
        movieRepository.trendingMovies(deviceLanguage)
    }

    private val topRated = deviceLanguage.map { deviceLanguage ->
        movieRepository.topRatedMovies(deviceLanguage)
    }

    private val nowPlaying = deviceLanguage.map { deviceLanguage ->
        movieRepository.nowPlayingMovies(deviceLanguage).map { pagingDate ->
            pagingDate.filterCompleteInfo()
        }
    }

    private val favourites: Flow<PagingData<MovieFavourite>> =
        favouritesRepository.favouriteMovies().cachedIn(viewModelScope)

    private val recentBrowsed: Flow<PagingData<RecentlyBrowsedMovie>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies().cachedIn(viewModelScope)

    private val moviesState: Flow<MoviesState> = combine(
        discover, upcoming, trending, topRated, nowPlaying
    ) { discover, upcoming, trending, topRated, nowPlaying ->
        MoviesState(
            discover = discover,
            upcoming = upcoming,
            trending = trending,
            topRated = topRated,
            nowPlaying = nowPlaying
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MoviesState.default)

    val uiState: StateFlow<MovieScreenUiState> = moviesState.map { moviesState ->
        MovieScreenUiState(
            moviesState = moviesState,
            favourites = favourites,
            recentlyBrowsed = recentBrowsed
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