package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetRelatedMoviesOfTypeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRelatedMoviesOfTypeUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetRelatedMoviesOfTypeUseCase {
    override operator fun invoke(
        movieId: Int,
        type: RelationType,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> {
        return when (type) {
            RelationType.Similar -> {
                movieRepository.similarMovies(
                    movieId = movieId,
                    deviceLanguage = deviceLanguage
                )
            }

            RelationType.Recommended -> {
                movieRepository.moviesRecommendations(
                    movieId = movieId,
                    deviceLanguage = deviceLanguage
                )
            }
        }
    }
}