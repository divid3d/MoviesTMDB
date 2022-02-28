package com.example.moviesapp.repository.config

import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Genre
import com.example.moviesapp.model.ProviderSource
import com.example.moviesapp.other.ImageUrlParser
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun isInitialised(): Flow<Boolean>

    fun updateLocale()

    fun getSpeechToTextAvailable(): Flow<Boolean>

    fun getDeviceLanguage(): Flow<DeviceLanguage>

    fun getImageUrlParser(): Flow<ImageUrlParser?>

    fun getMovieGenres(): Flow<List<Genre>>

    fun getTvSeriesGenres(): Flow<List<Genre>>

    fun getAllMoviesWatchProviders(): Flow<List<ProviderSource>>

    fun getAllTvSeriesWatchProviders(): Flow<List<ProviderSource>>
}