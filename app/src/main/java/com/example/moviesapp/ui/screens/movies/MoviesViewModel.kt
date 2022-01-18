package com.example.moviesapp.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.getImageUrl
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val favouritesRepository: FavouritesRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val config = configRepository.config

    val discover: Flow<PagingData<Presentable>> =
        movieRepository.discoverMovies().combine(config) { pagingData, config ->
            pagingData.map { movie -> movie.appendUrls(config) }
        }

    val upcoming: Flow<PagingData<Presentable>> =
        movieRepository.upcomingMovies().combine(config) { pagingData, config ->
            pagingData.map { movie -> movie.appendUrls(config) }
        }

    val topRated: Flow<PagingData<Presentable>> =
        movieRepository.topRatedMovies().combine(config) { pagingData, config ->
            pagingData.map { movie -> movie.appendUrls(config) }
        }

    val nowPlaying: Flow<PagingData<Presentable>> =
        movieRepository.nowPlayingMovies().combine(config) { pagingData, config ->
            pagingData
                .filter { tvSeries ->
                    tvSeries.run {
                        !backdropPath.isNullOrEmpty() && !posterPath.isNullOrEmpty() && title.isNotEmpty() && overview.isNotEmpty()
                    }
                }
                .map { movie -> movie.appendUrls(config) }
        }

    val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouriteMovies().combine(config) { pagingData, config ->
            pagingData.map { movie -> movie.appendUrls(config) }
        }

    private fun Movie.appendUrls(
        config: Config?
    ): Movie {
        val moviePosterUrl = config?.getImageUrl(posterPath)
        val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

        return copy(
            posterUrl = moviePosterUrl,
            backdropUrl = movieBackdropUrl
        )
    }

    private fun MovieFavourite.appendUrls(
        config: Config?
    ): MovieFavourite {
        val moviePosterUrl = config?.getImageUrl(posterPath)
        val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

        return copy(
            posterUrl = moviePosterUrl,
            backdropUrl = movieBackdropUrl
        )
    }

}