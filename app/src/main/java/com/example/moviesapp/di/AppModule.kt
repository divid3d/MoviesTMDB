package com.example.moviesapp.di

import android.content.Context
import androidx.paging.DataSource
import androidx.room.Room
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.api.Timeouts
import com.example.moviesapp.api.TmdbApi
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.api.TmdbApiHelperImpl
import com.example.moviesapp.db.FavouritesDatabase
import com.example.moviesapp.db.FavouritesMoviesDao
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.other.ApiParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
object AppModule {

    @Provides
    @Singleton
    fun provideExternalCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

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

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FavouritesDatabase::class.java, "favourites").build()

    @Singleton
    @Provides
    fun provideFavouritesMoviesDao(database: FavouritesDatabase): FavouritesMoviesDao =
        database.favouritesMoviesDao()

    @Singleton
    @Provides
    fun provideFavouriteMoviesDataSource(dao: FavouritesMoviesDao): DataSource.Factory<Int, MovieFavourite> =
        dao.favouriteMovies()

//    @Singleton
//    @Provides
//    fun provideTopRatedMoviesDataSource(apiHelper: TmdbApiHelper) =
//        TopRatedMoviesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun provideDiscoverMoviesDataSource(apiHelper: TmdbApiHelper) =
//        DiscoverMoviesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun provideNowPlayingMoviesDataSource(apiHelper: TmdbApiHelper) =
//        NowPlayingMoviesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun provideUpcomingMoviesDataSource(apiHelper: TmdbApiHelper) =
//        UpcomingMoviesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun provideTopRatedTvSeriesDataSource(apiHelper: TmdbApiHelper) =
//        TopRatedTvSeriesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun provideOnTheAirTvSeriesDataSource(apiHelper: TmdbApiHelper) =
//        OnTheAirTvSeriesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun providePopularTvSeriesDataSource(apiHelper: TmdbApiHelper) =
//        PopularTvSeriesDataSource(apiHelper)
//
//    @Singleton
//    @Provides
//    fun provideAiringTodayTvSeriesDataSource(apiHelper: TmdbApiHelper) =
//        AiringTodayTvSeriesDataSource(apiHelper)

}