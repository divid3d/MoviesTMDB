package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Video
import com.example.moviesapp.repository.season.SeasonRepository
import com.example.moviesapp.use_case.interfaces.GetSeasonsVideosUseCase
import javax.inject.Inject

class GetSeasonsVideosUseCaseImpl @Inject constructor(
    private val seasonRepository: SeasonRepository
) : GetSeasonsVideosUseCase {
    override suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<List<Video>> {
        val response = seasonRepository.seasonVideos(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber
        ).awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val videos = response.data?.results?.sortedWith(
                    compareBy<Video> { video ->
                        video.language == deviceLanguage.languageCode
                    }.thenByDescending { video ->
                        video.publishedAt
                    }
                )
                ApiResponse.Success(videos)
            }

            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}