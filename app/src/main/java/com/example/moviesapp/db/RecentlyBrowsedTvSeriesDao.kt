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

    @Query("SELECT COUNT(id) FROM RecentlyBrowsedTvSeries")
    suspend fun recentlyBrowsedTvSeriesCount(): Int

    @Query("DELETE FROM RecentlyBrowsedTvSeries WHERE id IN (SELECT id FROM RecentlyBrowsedTvSeries ORDER BY added_date ASC LIMIT :limit)")
    suspend fun deleteLast(limit: Int = 1)

    suspend fun deleteAndAdd(
        vararg recentlyBrowsedTvSeries: RecentlyBrowsedTvSeries,
        maxItems: Int = 100
    ) {
        val addCount = recentlyBrowsedTvSeries.count()
        val currentCount = recentlyBrowsedTvSeriesCount()

        val removeCount = (currentCount + addCount - maxItems).coerceAtLeast(0)

        deleteLast(removeCount)
        addRecentlyBrowsedTvSeries(*recentlyBrowsedTvSeries)
    }

    @Query("DELETE FROM RecentlyBrowsedTvSeries")
    suspend fun clear()

}