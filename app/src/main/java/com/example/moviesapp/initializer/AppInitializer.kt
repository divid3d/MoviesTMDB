package com.example.moviesapp.initializer

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}