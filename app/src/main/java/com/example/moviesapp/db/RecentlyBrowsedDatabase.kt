package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.model.RecentlyBrowsedMovie
import com.example.moviesapp.model.RecentlyBrowsedTvSeries
import com.example.moviesapp.other.Converters

@Database(entities = [RecentlyBrowsedMovie::class, RecentlyBrowsedTvSeries::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecentlyBrowsedDatabase : RoomDatabase() {

    abstract fun recentlyBrowsedMovies(): RecentlyBrowsedMoviesDao

    abstract fun recentlyBrowsedTvSeries(): RecentlyBrowsedTvSeriesDao

}