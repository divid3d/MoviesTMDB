package com.example.moviesapp.db

import androidx.room.*
import com.example.moviesapp.model.TvSeriesDetailsRemoteKey
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface TvSeriesDetailsRemoteKeysDao {
    @Query("SELECT * FROM TvSeriesDetailsRemoteKey LIMIT 1")
    suspend fun getRemoteKey(): TvSeriesDetailsRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: TvSeriesDetailsRemoteKey)

    @Query("DELETE FROM TvSeriesDetailsRemoteKey")
    suspend fun deleteKeys()
}