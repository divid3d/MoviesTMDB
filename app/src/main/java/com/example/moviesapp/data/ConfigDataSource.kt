package com.example.moviesapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Genre
import com.example.moviesapp.model.ProviderSource
import com.example.moviesapp.other.ImageUrlParser
import com.google.firebase.crashlytics.FirebaseCrashlytics
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

    private val _movieWatchProviders: MutableStateFlow<List<ProviderSource>> =
        MutableStateFlow(emptyList())
    val movieWatchProviders: StateFlow<List<ProviderSource>> = _movieWatchProviders.asStateFlow()

    private val _tvSeriesWatchProviders: MutableStateFlow<List<ProviderSource>> =
        MutableStateFlow(emptyList())
    val tvSeriesWatchProviders: StateFlow<List<ProviderSource>> =
        _tvSeriesWatchProviders.asStateFlow()

    fun init() {
        apiHelper.getConfig().request { response ->
            response.onSuccess {
                externalScope.launch(defaultDispatcher) {
                    val config = data
                    _config.emit(config)
                }
            }

            response.onException {
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }

        externalScope.launch(defaultDispatcher) {
            deviceLanguage.collectLatest { deviceLanguage ->
                apiHelper.getMoviesGenres(isoCode = deviceLanguage.languageCode)
                    .request { response ->
                        response.onSuccess {
                            externalScope.launch(defaultDispatcher) {
                                val movieGenres = data?.genres

                                _movieGenres.emit(movieGenres ?: emptyList())
                            }
                        }

                        response.onException {
                            FirebaseCrashlytics.getInstance().recordException(exception)
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

                        response.onException {
                            FirebaseCrashlytics.getInstance().recordException(exception)
                        }
                    }

                apiHelper.getAllMoviesWatchProviders(
                    isoCode = deviceLanguage.languageCode,
                    region = deviceLanguage.region
                ).request { response ->
                    response.onSuccess {
                        externalScope.launch(defaultDispatcher) {
                            val watchProviders = data?.results?.sortedBy { provider ->
                                provider.displayPriority
                            }

                            _movieWatchProviders.emit(watchProviders ?: emptyList())
                        }
                    }

                    response.onException {
                        FirebaseCrashlytics.getInstance().recordException(exception)
                    }
                }

                apiHelper.getAllTvSeriesWatchProviders(
                    isoCode = deviceLanguage.languageCode,
                    region = deviceLanguage.region
                ).request { response ->
                    response.onSuccess {
                        externalScope.launch(defaultDispatcher) {
                            val watchProviders = data?.results?.sortedBy { provider ->
                                provider.displayPriority
                            }

                            _tvSeriesWatchProviders.emit(watchProviders ?: emptyList())
                        }
                    }

                    response.onException {
                        FirebaseCrashlytics.getInstance().recordException(exception)
                    }
                }
            }
        }
    }

}