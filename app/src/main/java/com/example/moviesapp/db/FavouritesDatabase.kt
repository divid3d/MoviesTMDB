package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.TvSeriesFavourite
import com.example.moviesapp.other.Converters

@Database(entities = [MovieFavourite::class, TvSeriesFavourite::class], version = 4)
@TypeConverters(Converters::class)
abstract class FavouritesDatabase : RoomDatabase() {

    abstract fun favouritesMoviesDao(): FavouritesMoviesDao

    abstract fun favouritesTvSeriesDao(): FavouritesTvSeriesDao
}