package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.RecentlyBrowsedMovie

@Dao
interface RecentBrowsedMoviesDao {

    @Query("SELECT * FROM RecentlyBrowsedMovie ORDER BY added_date DESC")
    fun recentBrowsedMovie(): DataSource.Factory<Int, RecentlyBrowsedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBrowsedMovie(vararg recentlyBrowsedMovie: RecentlyBrowsedMovie)

}