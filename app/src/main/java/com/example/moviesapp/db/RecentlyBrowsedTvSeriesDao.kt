package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.*
import com.example.moviesapp.model.RecentlyBrowsedTvSeries

@Dao
interface RecentlyBrowsedTvSeriesDao {

    @Query("SELECT * FROM RecentlyBrowsedTvSeries ORDER BY added_date DESC")
    fun recentBrowsedTvSeries(): DataSource.Factory<Int, RecentlyBrowsedTvSeries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentlyBrowsedTvSeries(vararg recentlyBrowsedMovie: RecentlyBrowsedTvSeries)

    @Query("SELECT COUNT(id) FROM RecentlyBrowsedTvSeries")
    suspend fun recentlyBrowsedTvSeriesCount(): Int

    @Query("DELETE FROM RecentlyBrowsedTvSeries WHERE added_date = (SELECT MIN(added_date) FROM RecentlyBrowsedTvSeries)")
    suspend fun deleteLast()

    @Transaction
    suspend fun deleteAndAdd(
        vararg recentlyBrowsedTvSeries: RecentlyBrowsedTvSeries,
        maxItems: Int = 100
    ) {
        val addCount = recentlyBrowsedTvSeries.count()
        val currentCount = recentlyBrowsedTvSeriesCount()

        val removeCount = (currentCount + addCount - maxItems).coerceAtLeast(0)

        repeat(removeCount) {
            deleteLast()
        }

        addRecentlyBrowsedTvSeries(*recentlyBrowsedTvSeries)
    }

}