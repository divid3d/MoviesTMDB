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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
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

    val discover: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.discoverMovies(deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val upcoming: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.upcomingMovies(deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val trending: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.trendingMovies(deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val topRated: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.topRatedMovies(deviceLanguage)
    }.flattenMerge().cachedIn(viewModelScope)

    val nowPlaying: Flow<PagingData<Movie>> = deviceLanguage.map { deviceLanguage ->
        movieRepository.nowPlayingMovies(deviceLanguage)
    }.flattenMerge().map { pagingData ->
        pagingData.filterCompleteInfo()
    }.cachedIn(viewModelScope)

    val favourites: Flow<PagingData<MovieFavourite>> =
        favouritesRepository.favouriteMovies().cachedIn(viewModelScope)

    val recentBrowsed: Flow<PagingData<RecentlyBrowsedMovie>> =
        recentlyBrowsedRepository.recentlyBrowsedMovies().cachedIn(viewModelScope)

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