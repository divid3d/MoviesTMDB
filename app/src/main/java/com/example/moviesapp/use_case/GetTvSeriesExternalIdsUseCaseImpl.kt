package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.ExternalContentType
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTvSeriesExternalIdsUseCase
import javax.inject.Inject

class GetTvSeriesExternalIdsUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTvSeriesExternalIdsUseCase {
    override suspend fun invoke(tvSeriesId: Int): ApiResponse<List<ExternalId>> {
        val response = tvSeriesRepository
            .getExternalIds(tvSeriesId)
            .awaitApiResponse()

        return when (response) {
            is ApiResponse.Success -> {
                val externalIds = response.data?.toExternalIdList(type = ExternalContentType.Tv)
                ApiResponse.Success(externalIds)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(response.apiError)
            is ApiResponse.Exception -> ApiResponse.Exception(response.exception)
        }
    }
}