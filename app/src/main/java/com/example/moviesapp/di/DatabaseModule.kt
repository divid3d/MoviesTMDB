package com.example.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviesapp.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFavouritesMoviesDao(database: AppDatabase): FavouritesMoviesDao =
        database.favouritesMoviesDao()

    @Singleton
    @Provides
    fun provideFavouriteTvSeriesDao(database: AppDatabase): FavouritesTvSeriesDao =
        database.favouritesTvSeriesDao()


    @Singleton
    @Provides
    fun provideRecentlyBrowsedMoviesDao(database: AppDatabase): RecentlyBrowsedMoviesDao =
        database.recentlyBrowsedMovies()

    @Singleton
    @Provides
    fun provideRecentlyBrowsedTvSeriesDao(database: AppDatabase): RecentlyBrowsedTvSeriesDao =
        database.recentlyBrowsedTvSeries()


    @Singleton
    @Provides
    fun provideSearchQueryDao(database: AppDatabase): SearchQueryDao =
        database.searchQueryDao()

    @Singleton
    @Provides
    fun provideMoviesDao(database: AppDatabase): MoviesDao = database.movieDao()
}