package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Video
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesVideosUseCase
import javax.inject.Inject

class GetTvSeriesVideosUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTvSeriesVideosUseCase {
    override suspend fun invoke(
        tvSeriesId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<List<Video>> {
        val response = tvSeriesRepository.tvSeriesVideos(
            tvSeriesId = tvSeriesId,
            isoCode = deviceLanguage.languageCode
        ).awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val videos = response.data?.results?.sortedWith(
                    compareBy(
                        { video -> video.official },
                        { video -> video.publishedAt }
                    )
                ) ?: emptyList()

                ApiResponse.Success(videos)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}