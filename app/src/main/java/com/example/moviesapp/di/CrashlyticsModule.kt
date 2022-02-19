package com.example.moviesapp.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Module
@InstallIn(SingletonComponent::class)
object CrashlyticsModule {

    @Provides
    @Singleton
    fun provideCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
}