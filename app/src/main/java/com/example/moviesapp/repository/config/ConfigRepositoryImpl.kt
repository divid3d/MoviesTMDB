package com.example.moviesapp.repository.config

import com.example.moviesapp.data.ConfigDataSource
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Genre
import com.example.moviesapp.model.ProviderSource
import com.example.moviesapp.other.ImageUrlParser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val configDataSource: ConfigDataSource
) : ConfigRepository {
    override fun isInitialised(): Flow<Boolean> = configDataSource.isInitialized

    override fun updateLocale() = configDataSource.updateLocale()

    override fun getSpeechToTextAvailable(): Flow<Boolean> = configDataSource.speechToTextAvailable

    override fun getDeviceLanguage(): Flow<DeviceLanguage> = configDataSource.deviceLanguage

    override fun getImageUrlParser(): Flow<ImageUrlParser?> = configDataSource.imageUrlParser

    override fun getMovieGenres(): Flow<List<Genre>> = configDataSource.movieGenres

    override fun getTvSeriesGenres(): Flow<List<Genre>> = configDataSource.tvSeriesGenres

    override fun getAllMoviesWatchProviders(): Flow<List<ProviderSource>> =
        configDataSource.movieWatchProviders

    override fun getAllTvSeriesWatchProviders(): Flow<List<ProviderSource>> =
        configDataSource.tvSeriesWatchProviders
}