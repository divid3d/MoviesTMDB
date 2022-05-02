package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.model.*
import com.example.moviesapp.other.DateConverters

@Database(
    entities = [
        MovieFavourite::class,
        TvSeriesFavourite::class,
        RecentlyBrowsedMovie::class,
        RecentlyBrowsedTvSeries::class,
        SearchQuery::class,
        MovieEntity::class,
        MoviesRemoteKeys::class,
        TvSeriesEntity::class,
        TvSeriesRemoteKeys::class,
        MovieDetailEntity::class,
        MovieDetailsRemoteKey::class,
        TvSeriesDetailEntity::class,
        TvSeriesDetailsRemoteKey::class
    ],
    version = 1
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritesMoviesDao(): FavouritesMoviesDao
    abstract fun favouritesTvSeriesDao(): FavouritesTvSeriesDao
    abstract fun recentlyBrowsedMovies(): RecentlyBrowsedMoviesDao
    abstract fun recentlyBrowsedTvSeries(): RecentlyBrowsedTvSeriesDao
    abstract fun searchQueryDao(): SearchQueryDao
    abstract fun movieDao(): MoviesDao
    abstract fun moviesRemoteKeysDao(): MoviesRemoteKeysDao
    abstract fun tvSeriesDao(): TvSeriesDao
    abstract fun tvSeriesRemoteKeysDao(): TvSeriesRemoteKeysDao
    abstract fun moviesDetailsDao(): MoviesDetailsDao
    abstract fun moviesDetailsRemoteKeys(): MoviesDetailsRemoteKeysDao
    abstract fun tvSeriesDetailsDao(): TvSeriesDetailsDao
    abstract fun tvSeriesDetailsRemoteKeys(): TvSeriesDetailsRemoteKeysDao
}