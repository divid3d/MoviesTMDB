package com.example.moviesapp.repository

import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val externalScope: CoroutineScope,
    private val apiHelper: TmdbApiHelper
) {
    private val _config: MutableStateFlow<Config?> = MutableStateFlow(null)
    val config: StateFlow<Config?> = _config.asStateFlow()

    fun init() {
        externalScope.launch {
            val config = apiHelper.getConfig()

            _config.emit(config)
        }
    }
}