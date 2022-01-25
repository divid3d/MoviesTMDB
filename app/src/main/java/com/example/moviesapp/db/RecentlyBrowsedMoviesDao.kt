package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.RecentlyBrowsedMovie

@Dao
interface RecentlyBrowsedMoviesDao {

    @Query("SELECT * FROM RecentlyBrowsedMovie ORDER BY added_date DESC")
    fun recentBrowsedMovie(): DataSource.Factory<Int, RecentlyBrowsedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentlyBrowsedMovie(vararg recentlyBrowsedMovie: RecentlyBrowsedMovie)

}