package com.example.moviesapp.ui.screens.allMovies

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class AllMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favouritesRepository: FavouritesRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val config = configRepository.config

    private val movieType: Flow<MovieType> = savedStateHandle
        .getLiveData("movieType", MovieType.Popular.name)
        .asFlow().map { value ->
            MovieType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> =
        movieRepository.topRatedMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    private val upcoming: Flow<PagingData<Presentable>> =
        movieRepository.upcomingMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    private val popular: Flow<PagingData<Presentable>> =
        movieRepository.discoverMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }
    private val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { favouriteMovie -> favouriteMovie.appendUrls(config) }
            }

    val movies: Flow<PagingData<Presentable>> = combine(
        movieType,
        topRated,
        upcoming,
        popular,
        favourites
    ) { type, topRated, upcoming, popular, favourites ->
        when (type) {
            MovieType.TopRated -> topRated
            MovieType.Upcoming -> upcoming
            MovieType.Popular -> popular
            MovieType.Favourite -> favourites
        }
    }

    val favouriteMoviesCount: StateFlow<Int> = favouritesRepository.getFavouriteMoviesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), 0)


}