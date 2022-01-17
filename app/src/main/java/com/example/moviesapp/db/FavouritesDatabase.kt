package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.model.MovieFavourite

@Database(entities = [MovieFavourite::class], version = 2)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun favouritesMoviesDao(): FavouritesMoviesDao
}