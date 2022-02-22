package com.example.moviesapp.repository

import com.example.moviesapp.data.ConfigDataSource
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Genre
import com.example.moviesapp.model.ProviderSource
import com.example.moviesapp.other.ImageUrlParser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val configDataSource: ConfigDataSource
) {
    fun isInitialised(): Flow<Boolean> = configDataSource.isInitialized

    fun getSpeechToTextAvailable(): Flow<Boolean> = configDataSource.speechToTextAvailable

    fun getDeviceLanguage(): Flow<DeviceLanguage> = configDataSource.deviceLanguage

    fun getImageUrlParser(): Flow<ImageUrlParser?> = configDataSource.imageUrlParser

    fun getMovieGenres(): Flow<List<Genre>> = configDataSource.movieGenres

    fun getTvSeriesGenres(): Flow<List<Genre>> = configDataSource.tvSeriesGenres

    fun getAllMoviesWatchProviders(): Flow<List<ProviderSource>> =
        configDataSource.movieWatchProviders

    fun getAllTvSeriesWatchProviders(): Flow<List<ProviderSource>> =
        configDataSource.tvSeriesWatchProviders
}