package com.example.moviesapp.di

import com.example.moviesapp.BuildConfig
import com.example.moviesapp.api.Timeouts
import com.example.moviesapp.api.TmdbApi
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.api.TmdbApiHelperImpl
import com.example.moviesapp.other.ApiParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@OptIn(ExperimentalTime::class)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestUrl = request.url
            val url = requestUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

            val modifiedRequest = request.newBuilder()
                .url(url)
                .build()
            chain.proceed(modifiedRequest)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authenticationInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
        }
        .addInterceptor(authenticationInterceptor)
        .connectTimeout(Timeouts.connect.toJavaDuration())
        .writeTimeout(Timeouts.write.toJavaDuration())
        .readTimeout(Timeouts.read.toJavaDuration())
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(ApiParams.baseUrl)
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideTmdbApi(retorfit: Retrofit): TmdbApi = retorfit.create(TmdbApi::class.java)

    @Singleton
    @Provides
    fun provideTmdbApiHelper(apiHelper: TmdbApiHelperImpl): TmdbApiHelper = apiHelper

}