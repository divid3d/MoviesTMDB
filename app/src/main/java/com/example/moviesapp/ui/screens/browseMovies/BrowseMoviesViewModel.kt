package com.example.moviesapp.ui.screens.browseMovies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
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
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val config = configRepository.config

    var movies: Flow<PagingData<Presentable>>? = null

    private val movieType: Flow<MovieType> = savedStateHandle
        .getLiveData("movieType", MovieType.Popular.name)
        .asFlow().map { value ->
            MovieType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> =
        movieRepository.topRatedMovies()
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    private val upcoming: Flow<PagingData<Presentable>> =
        movieRepository.upcomingMovies()
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    private val popular: Flow<PagingData<Presentable>> =
        movieRepository.discoverMovies()
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    private val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies()
            .combine(config) { pagingData, config ->
                pagingData.map { favouriteMovie -> favouriteMovie.appendUrls(config) }
            }

    private val recentlyBrowsed: Flow<PagingData<Presentable>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies()
            .combine(config) { pagingData, config ->
                pagingData.map { recentlyBrowsedMovie -> recentlyBrowsedMovie.appendUrls(config) }
            }

    private val trending: Flow<PagingData<Presentable>> =
        movieRepository.trendingMovies()
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    val favouriteMoviesCount: StateFlow<Int> = favouritesRepository.getFavouriteMoviesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)

    init {
        viewModelScope.launch {
            movieType.collectLatest { type ->
                movies = when (type) {
                    MovieType.TopRated -> topRated
                    MovieType.Upcoming -> upcoming
                    MovieType.Popular -> popular
                    MovieType.Favourite -> favourites
                    MovieType.RecentlyBrowsed -> recentlyBrowsed
                    MovieType.Trending -> trending
                }.cachedIn(viewModelScope)
            }
        }
    }

    fun onClearClicked() {
        recentlyBrowsedRepository.clear()
    }

}