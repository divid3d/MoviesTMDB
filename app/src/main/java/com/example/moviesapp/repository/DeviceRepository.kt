package com.example.moviesapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import com.example.moviesapp.model.DeviceLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
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

}