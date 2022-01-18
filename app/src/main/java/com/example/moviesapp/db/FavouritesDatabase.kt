package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.TvSeriesFavourite

@Database(entities = [MovieFavourite::class, TvSeriesFavourite::class], version = 3)
abstract class FavouritesDatabase : RoomDatabase() {

    abstract fun favouritesMoviesDao(): FavouritesMoviesDao

    abstract fun favouritesTvSeriesDao(): FavouritesTvSeriesDao
}