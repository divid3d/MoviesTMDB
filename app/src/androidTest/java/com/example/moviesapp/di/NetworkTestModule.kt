package com.example.moviesapp.di

import com.example.moviesapp.BuildConfig
import com.example.moviesapp.api.ApiParams
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@OptIn(ExperimentalTime::class)
@Module
@InstallIn(SingletonComponent::class)
object NetworkTestModule {

    @Singleton
    @Binds
    @Named("test_okhttp")
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
        }
        .connectTimeout(ApiParams.Timeouts.connect.toJavaDuration())
        .writeTimeout(ApiParams.Timeouts.write.toJavaDuration())
        .readTimeout(ApiParams.Timeouts.read.toJavaDuration())
        .build()
}