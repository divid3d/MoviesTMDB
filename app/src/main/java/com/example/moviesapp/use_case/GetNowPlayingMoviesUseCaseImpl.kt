package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.model.DetailPresentable
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieDetailEntity
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
    ): Flow<PagingData<DetailPresentable>> {
        return movieRepository.nowPlayingMovies(deviceLanguage).mapLatest { data ->
            if (filtered) data.filterCompleteInfo() else data
        }.mapLatest { data -> data.map { it } }
    }

    private fun PagingData<MovieDetailEntity>.filterCompleteInfo(): PagingData<MovieDetailEntity> {
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