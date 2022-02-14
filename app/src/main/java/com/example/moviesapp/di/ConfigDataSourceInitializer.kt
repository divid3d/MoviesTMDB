package com.example.moviesapp.di

import android.app.Application
import com.example.moviesapp.data.ConfigDataSource
import com.example.moviesapp.initializer.AppInitializer
import javax.inject.Inject

class ConfigDataSourceInitializer @Inject constructor(
    private val configDataSource: ConfigDataSource
) : AppInitializer {
    override fun init(application: Application) {
        configDataSource.init()
    }
}