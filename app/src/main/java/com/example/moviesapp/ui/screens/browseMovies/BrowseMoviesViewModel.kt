package com.example.moviesapp.ui.screens.browseMovies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.*
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.DeviceRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class BrowseMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val deviceRepository: DeviceRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = deviceRepository.deviceLanguage

    var movies: Flow<PagingData<Presentable>>? = null

    private val movieType: Flow<MovieType> = savedStateHandle
        .getLiveData("movieType", MovieType.Upcoming.name)
        .asFlow().map { value ->
            MovieType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.topRatedMovies(deviceLanguage = deviceLanguage)
    }.flattenMerge()

    private val upcoming: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.upcomingMovies(deviceLanguage = deviceLanguage)
    }.flattenMerge()

    private val trending: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.trendingMovies(deviceLanguage = deviceLanguage)
    }.flattenMerge()

    private val favourites: Flow<PagingData<MovieFavourite>> =
        favouritesRepository.favouriteMovies()

    private val recentlyBrowsed: Flow<PagingData<RecentlyBrowsedMovie>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies()

    val favouriteMoviesCount: StateFlow<Int> = favouritesRepository.getFavouriteMoviesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    init {
        viewModelScope.launch {
            movieType.collectLatest { type ->
                movies = when (type) {
                    MovieType.TopRated -> topRated
                    MovieType.Upcoming -> upcoming
                    MovieType.Favourite -> favourites
                    MovieType.RecentlyBrowsed -> recentlyBrowsed
                    MovieType.Trending -> trending
                }.map { data -> data.map { movie -> movie } }.cachedIn(viewModelScope)
            }
        }
    }

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedMovies()
    }

}