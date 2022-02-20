package com.example.moviesapp.ui.screens.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
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

    private val movieType: Flow<MovieType> = savedStateHandle
        .getLiveData("movieType", MovieType.Upcoming.name)
        .asFlow().map { value ->
            MovieType.valueOf(value)
        }

    val favouriteMoviesCount: StateFlow<Int> = favouritesRepository.getFavouriteMoviesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    val movies: Flow<PagingData<Presentable>> = combine(
        movieType, deviceLanguage
    ) { type, deviceLanguage ->
        when (type) {
            MovieType.TopRated -> movieRepository.topRatedMovies(deviceLanguage)
            MovieType.Upcoming -> movieRepository.upcomingMovies(deviceLanguage)
            MovieType.Favourite -> favouritesRepository.favouriteMovies()
            MovieType.RecentlyBrowsed -> recentlyBrowsedRepository.recentlyBrowsedMovies()
            MovieType.Trending -> movieRepository.trendingMovies(deviceLanguage)
        }
    }.flattenMerge().map { data -> data.map { movie -> movie } }.cachedIn(viewModelScope)

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedMovies()
    }
}