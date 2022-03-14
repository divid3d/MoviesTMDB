package com.example.moviesapp.initializer

import android.app.Application
import com.example.moviesapp.data.ConfigDataSource
import javax.inject.Inject

class ConfigDataSourceInitializer @Inject constructor(
    private val configDataSource: ConfigDataSource
) : AppInitializer {
    override fun init(application: Application) {
        configDataSource.init()
    }
}