package com.example.moviesapp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.api.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@OptIn(ExperimentalTime::class)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context) =
        Cache(context.cacheDir, ApiParams.cacheSize)

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestUrl = request.url
            val url = requestUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()

            val modifiedRequest = request.newBuilder()
                .url(url)
                .build()
            chain.proceed(modifiedRequest)
        }
    }

    @Singleton
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            .maxContentLength(250_000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache,
        authenticationInterceptor: Interceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
        }
        .addInterceptor(chuckerInterceptor)
        .addInterceptor(authenticationInterceptor)
        .cache(cache)
        .connectTimeout(ApiParams.Timeouts.connect.toJavaDuration())
        .writeTimeout(ApiParams.Timeouts.write.toJavaDuration())
        .readTimeout(ApiParams.Timeouts.read.toJavaDuration())
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, DateJsonAdapter().nullSafe())
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(ApiParams.secureBaseUrl.toHttpUrl())
            .client(client)
            .build()

    @Singleton
    @Provides
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)

    @Singleton
    @Provides
    fun provideTmdbApiHelper(apiHelper: TmdbApiHelperImpl): TmdbApiHelper = apiHelper

}