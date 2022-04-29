package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieCollection
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetMovieCollectionUseCase
import javax.inject.Inject

class GetMovieCollectionUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieCollectionUseCase {
    override suspend operator fun invoke(
        collectionId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<MovieCollection?> {
        val response = movieRepository.collection(
            collectionId = collectionId,
            isoCode = deviceLanguage.languageCode
        ).awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val collection = response.data?.let { collection ->
                    val name = collection.name
                    val parts = collection.parts

                    MovieCollection(
                        name = name,
                        parts = parts
                    )
                }
                ApiResponse.Success(collection)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}