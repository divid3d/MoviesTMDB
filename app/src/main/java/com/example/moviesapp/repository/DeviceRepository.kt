package com.example.moviesapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceRepository @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    @SuppressLint("QueryPermissionsNeeded")
    val speechToTextAvailable: Flow<Boolean> = flow {
        val packageManager = applicationContext.packageManager
        val activities: List<*> = packageManager.queryIntentActivities(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
            0
        )

        emit(activities.isNotEmpty())
    }.flowOn(Dispatchers.Default)


}