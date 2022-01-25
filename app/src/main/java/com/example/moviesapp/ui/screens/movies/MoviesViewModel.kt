package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val config = configRepository.config

    val discover: Flow<PagingData<Presentable>> =
        movieRepository.discoverMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    val upcoming: Flow<PagingData<Presentable>> =
        movieRepository.upcomingMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    val trending: Flow<PagingData<Presentable>> =
        movieRepository.trendingMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    val topRated: Flow<PagingData<Presentable>> =
        movieRepository.topRatedMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

    val nowPlaying: Flow<PagingData<Presentable>> =
        movieRepository.nowPlayingMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData
                    .filter { tvSeries ->
                        tvSeries.run {
                            !backdropPath.isNullOrEmpty() && !posterPath.isNullOrEmpty() && title.isNotEmpty() && overview.isNotEmpty()
                        }
                    }
                    .map { movie -> movie.appendUrls(config) }
            }

    val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }


    val recentBrowsed: Flow<PagingData<Presentable>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies()
            .cachedIn(viewModelScope)
            .combine(config) { pagingData, config ->
                pagingData.map { movie -> movie.appendUrls(config) }
            }

}