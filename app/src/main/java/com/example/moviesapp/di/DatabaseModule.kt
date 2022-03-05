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
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFavouritesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FavouritesDatabase::class.java, "favourites")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFavouritesMoviesDao(database: FavouritesDatabase): FavouritesMoviesDao =
        database.favouritesMoviesDao()

    @Singleton
    @Provides
    fun provideFavouriteTvSeriesDao(database: FavouritesDatabase): FavouritesTvSeriesDao =
        database.favouritesTvSeriesDao()


    @Singleton
    @Provides
    fun provideRecentBrowsedDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RecentlyBrowsedDatabase::class.java, "recent_browsed")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideRecentlyBrowsedMoviesDao(database: RecentlyBrowsedDatabase): RecentlyBrowsedMoviesDao =
        database.recentlyBrowsedMovies()

    @Singleton
    @Provides
    fun provideRecentlyBrowsedTvSeriesDao(database: RecentlyBrowsedDatabase): RecentlyBrowsedTvSeriesDao =
        database.recentlyBrowsedTvSeries()

    @Singleton
    @Provides
    fun provideQueryDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, QueryDatabase::class.java, "query")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideSearchQueryDao(database: QueryDatabase): SearchQueryDao =
        database.searchQueryDao()
}