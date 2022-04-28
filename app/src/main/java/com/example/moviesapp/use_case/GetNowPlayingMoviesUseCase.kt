package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.movie.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetNowPlayingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        deviceLanguage: DeviceLanguage,
        filtered: Boolean = false
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