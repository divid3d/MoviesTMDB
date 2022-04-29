package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetNowPlayingMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetNowPlayingMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetNowPlayingMoviesUseCase {
    override operator fun invoke(
        deviceLanguage: DeviceLanguage,
        filtered: Boolean
    ): Flow<PagingData<Movie>> {
        return movieRepository.nowPlayingMovies(deviceLanguage).apply {
            if (filtered) {
                mapLatest { pagingData -> pagingData.filterCompleteInfo() }
            }
        }
    }

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