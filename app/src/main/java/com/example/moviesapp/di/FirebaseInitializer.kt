package com.example.moviesapp.di

import android.app.Application
import com.example.moviesapp.initializer.AppInitializer
import com.google.firebase.FirebaseApp
import javax.inject.Inject

class FirebaseInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application) {
        FirebaseApp.initializeApp(application)
    }
}