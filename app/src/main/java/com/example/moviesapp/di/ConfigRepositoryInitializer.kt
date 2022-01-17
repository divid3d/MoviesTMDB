package com.example.moviesapp.di

import android.app.Application
import com.example.moviesapp.initializer.AppInitializer
import com.example.moviesapp.repository.ConfigRepository
import javax.inject.Inject

class ConfigRepositoryInitializer @Inject constructor(
    private val configRepository: ConfigRepository
) : AppInitializer {
    override fun init(application: Application) {
        configRepository.init()
    }
}