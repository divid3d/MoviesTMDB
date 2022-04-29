package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetDiscoverAllMoviesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoverAllMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetDiscoverAllMoviesUseCase {
    override operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<Movie>> {
        return movieRepository.discoverMovies(deviceLanguage)
    }
}