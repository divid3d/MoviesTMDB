package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.ExternalContentType
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.repository.movie.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMovieExternalIdsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
){
    suspend operator fun invoke(movieId: Int): ApiResponse<List<ExternalId>>{
        val response =  movieRepository.getExternalIds(movieId).awaitApiResponse()

        return when(response){
            is ApiResponse.Success -> {
                val ids =  response.data?.toExternalIdList(ExternalContentType.Movie)
                ApiResponse.Success(ids)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}