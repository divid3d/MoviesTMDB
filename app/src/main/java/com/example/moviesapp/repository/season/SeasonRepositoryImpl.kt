package com.example.moviesapp.repository.season

import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.*
import retrofit2.Call
import javax.inject.Singleton

@Singleton
class SeasonRepositoryImpl(
    private val apiHelper: TmdbApiHelper
) : SeasonRepository {
    override fun getTvSeason(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): Call<TvSeasonsResponse> =
        apiHelper.getTvSeasons(tvSeriesId, seasonNumber, deviceLanguage.languageCode)

    override fun seasonDetails(
        tvSeriesId: Int,
        seasonNumber: Int,
        deviceLanguage: DeviceLanguage
    ): Call<SeasonDetails> =
        apiHelper.getSeasonDetails(tvSeriesId, seasonNumber, deviceLanguage.languageCode)

    override fun episodeImages(
        tvSeriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Call<ImagesResponse> = apiHelper.getEpisodeImages(tvSeriesId, seasonNumber, episodeNumber)

    override fun seasonVideos(
        tvSeriesId: Int,
        seasonNumber: Int,
        isoCode: String
    ): Call<VideosResponse> = apiHelper.getSeasonVideos(tvSeriesId, seasonNumber, isoCode)

    override fun seasonCredits(tvSeriesId: Int, seasonNumber: Int, isoCode: String) =
        apiHelper.getSeasonCredits(tvSeriesId, seasonNumber, isoCode)
}