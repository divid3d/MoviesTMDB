package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetTopRatedMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetTopRatedMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetTopRatedMoviesUseCase {
    override operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<Presentable>> {
        return movieRepository.topRatedMovies(deviceLanguage)
            .mapLatest { data -> data.map { it } }
    }
}