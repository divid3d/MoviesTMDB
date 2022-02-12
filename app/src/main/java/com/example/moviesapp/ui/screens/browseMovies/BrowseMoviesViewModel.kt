package com.example.moviesapp.ui.screens.browseMovies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.asFlow
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
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var movies: Flow<PagingData<Presentable>>? = null

    private val movieType: Flow<MovieType> = savedStateHandle
        .getLiveData("movieType", MovieType.Upcoming.name)
        .asFlow().map { value ->
            MovieType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> =
        movieRepository.topRatedMovies().map { data -> data.map { movie -> movie } }


    private val upcoming: Flow<PagingData<Presentable>> =
        movieRepository.upcomingMovies().map { data -> data.map { movie -> movie } }


    private val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies().map { data -> data.map { movie -> movie } }


    private val recentlyBrowsed: Flow<PagingData<Presentable>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies()
            .map { data -> data.map { movie -> movie } }


    private val trending: Flow<PagingData<Presentable>> =
        movieRepository.trendingMovies().map { data -> data.map { movie -> movie } }


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
                }.cachedIn(viewModelScope)
            }
        }
    }

    fun onClearClicked() {
        recentlyBrowsedRepository.clearRecentlyBrowsedMovies()
    }

}