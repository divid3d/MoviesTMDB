package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.RecentlyBrowsedTvSeries

@Dao
interface RecentlyBrowsedTvSeriesDao {

    @Query("SELECT * FROM RecentlyBrowsedTvSeries ORDER BY added_date DESC")
    fun recentBrowsedTvSeries(): DataSource.Factory<Int, RecentlyBrowsedTvSeries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentlyBrowsedTvSeries(vararg recentlyBrowsedMovie: RecentlyBrowsedTvSeries)

}