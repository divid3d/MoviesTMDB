package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.other.Converters

@Database(entities = [SearchQuery::class], version = 4)
@TypeConverters(Converters::class)
abstract class QueryDatabase : RoomDatabase() {

    abstract fun searchQueryDao(): SearchQueryDao

}