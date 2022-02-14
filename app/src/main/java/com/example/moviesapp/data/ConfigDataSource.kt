package com.example.moviesapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Genre
import com.example.moviesapp.other.ImageUrlParser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigDataSource @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper
) {
    private val _config: MutableStateFlow<Config?> = MutableStateFlow(null)

    @SuppressLint("QueryPermissionsNeeded")
    val speechToTextAvailable: Flow<Boolean> = flow {
        val packageManager = context.packageManager
        val activities: List<*> = packageManager.queryIntentActivities(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
            0
        )

        emit(activities.isNotEmpty())
    }.flowOn(Dispatchers.Default)

    val deviceLanguage: Flow<DeviceLanguage> = flow {
        val locale = Locale.getDefault()

        val languageCode = locale.toLanguageTag().ifEmpty { DeviceLanguage.default.languageCode }
        val region = locale.country.ifEmpty { DeviceLanguage.default.region }

        val deviceLanguage = DeviceLanguage(
            languageCode = languageCode,
            region = region
        )

        emit(deviceLanguage)
    }.flowOn(Dispatchers.Default)

    val imageUrlParser: Flow<ImageUrlParser?> = _config.map { config ->
        if (config != null) {
            ImageUrlParser(config.imagesConfig)
        } else null
    }.flowOn(defaultDispatcher)

    private val _movieGenres: MutableStateFlow<List<Genre>> = MutableStateFlow(emptyList())
    val movieGenres: StateFlow<List<Genre>> = _movieGenres.asStateFlow()

    private val _tvSeriesGenres: MutableStateFlow<List<Genre>> = MutableStateFlow(emptyList())
    val tvSeriesGenres: StateFlow<List<Genre>> = _tvSeriesGenres.asStateFlow()

    fun init() {
        apiHelper.getConfig().request { response ->
            response.onSuccess {
                externalScope.launch(defaultDispatcher) {
                    val config = data
                    _config.emit(config)
                }
            }
        }

        externalScope.launch {
            deviceLanguage.collectLatest { deviceLanguage ->
                apiHelper.getMoviesGenres(isoCode = deviceLanguage.languageCode)
                    .request { response ->
                        response.onSuccess {
                            externalScope.launch(defaultDispatcher) {
                                val movieGenres = data?.genres

                                _movieGenres.emit(movieGenres ?: emptyList())
                            }
                        }
                    }

                apiHelper.getTvSeriesGenres(isoCode = deviceLanguage.languageCode)
                    .request { response ->
                        response.onSuccess {
                            externalScope.launch(defaultDispatcher) {
                                val tvSeriesGenres = data?.genres

                                _tvSeriesGenres.emit(tvSeriesGenres ?: emptyList())
                            }
                        }
                    }
            }
        }
    }

}