package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRelatedMoviesOfTypeUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(
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