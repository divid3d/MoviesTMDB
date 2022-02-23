package com.example.moviesapp.ui.screens.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.ui.screens.destinations.BrowseMoviesScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class BrowseMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    private val navArgs: BrowseMoviesScreenArgs =
        BrowseMoviesScreenDestination.argsFrom(savedStateHandle)

    private val favouriteMoviesCount: StateFlow<Int> =
        favouritesRepository.getFavouriteMoviesCount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    val uiState: StateFlow<BrowseMoviesScreenUiState> = combine(
        deviceLanguage, favouriteMoviesCount
    ) { deviceLanguage, favouriteMoviesCount ->
        val movies = when (navArgs.movieType) {
            MovieType.TopRated -> movieRepository.topRatedMovies(deviceLanguage)
            MovieType.Upcoming -> movieRepository.upcomingMovies(deviceLanguage)
            MovieType.Favourite -> favouritesRepository.favouriteMovies()
            MovieType.RecentlyBrowsed -> recentlyBrowsedRepository.recentlyBrowsedMovies()
            MovieType.Trending -> movieRepository.trendingMovies(deviceLanguage)
        }.map { data -> data.map { movie -> movie } }

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

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedMovies()
    }
}