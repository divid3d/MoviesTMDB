package com.example.moviesapp.repository

import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.Genre
import com.example.moviesapp.other.ImageUrlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper
) {
    private val _config: MutableStateFlow<Config?> = MutableStateFlow(null)

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

        apiHelper.getMoviesGenres().request { response ->
            response.onSuccess {
                externalScope.launch(defaultDispatcher) {
                    val movieGenres = data?.genres

                    _movieGenres.emit(movieGenres ?: emptyList())
                }
            }
        }

        apiHelper.getTvSeriesGenres().request { response ->
            response.onSuccess {
                externalScope.launch(defaultDispatcher) {
                    val tvSeriesGenres = data?.genres

                    _tvSeriesGenres.emit(tvSeriesGenres ?: emptyList())
                }
            }
        }
    }
}