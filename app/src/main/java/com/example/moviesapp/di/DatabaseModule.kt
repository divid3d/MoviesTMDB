package com.example.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviesapp.db.FavouritesDatabase
import com.example.moviesapp.db.FavouritesMoviesDao
import com.example.moviesapp.db.FavouritesTvSeriesDao
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
    fun provideDatabase(@ApplicationContext context: Context) =
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

}