package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.*
import com.example.moviesapp.model.RecentlyBrowsedMovie

@Dao
interface RecentlyBrowsedMoviesDao {

    @Query("SELECT * FROM RecentlyBrowsedMovie ORDER BY added_date DESC")
    fun recentBrowsedMovie(): DataSource.Factory<Int, RecentlyBrowsedMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentlyBrowsedMovie(vararg recentlyBrowsedMovie: RecentlyBrowsedMovie)

    @Query("SELECT COUNT(id) FROM RecentlyBrowsedMovie")
    suspend fun recentlyBrowsedMovieCount(): Int

    @Query("DELETE FROM RecentlyBrowsedMovie WHERE added_date = (SELECT MIN(added_date) FROM RecentlyBrowsedMovie)")
    suspend fun deleteLast()

    @Transaction
    suspend fun deleteAndAdd(
        vararg recentlyBrowsedMovie: RecentlyBrowsedMovie,
        maxItems: Int = 100
    ) {
        val addCount = recentlyBrowsedMovie.count()
        val currentCount = recentlyBrowsedMovieCount()

        val removeCount = (currentCount + addCount - maxItems).coerceAtLeast(0)

        repeat(removeCount) {
            deleteLast()
        }

        addRecentlyBrowsedMovie(*recentlyBrowsedMovie)
    }

    @Query("DELETE FROM RecentlyBrowsedMovie")
    suspend fun clear()

}