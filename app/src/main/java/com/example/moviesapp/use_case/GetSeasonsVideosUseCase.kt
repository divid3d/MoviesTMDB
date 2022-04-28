package com.example.moviesapp.use_case

import androidx.paging.PagingSource
import com.example.moviesapp.api.ApiError
import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Video
import com.example.moviesapp.model.VideosResponse
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.JsonDataException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSeasonsVideosUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) {
    suspend operator fun invoke(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<List<Video>> {
        val response = tvSeriesRepository.seasonVideos(
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