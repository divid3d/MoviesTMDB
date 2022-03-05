package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.model.*
import com.example.moviesapp.other.Converters

@Database(
    entities = [
        MovieFavourite::class,
        TvSeriesFavourite::class,
        RecentlyBrowsedMovie::class,
        RecentlyBrowsedTvSeries::class,
        SearchQuery::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouritesMoviesDao(): FavouritesMoviesDao

    abstract fun favouritesTvSeriesDao(): FavouritesTvSeriesDao

    abstract fun recentlyBrowsedMovies(): RecentlyBrowsedMoviesDao

    abstract fun recentlyBrowsedTvSeries(): RecentlyBrowsedTvSeriesDao

    abstract fun searchQueryDao(): SearchQueryDao
}