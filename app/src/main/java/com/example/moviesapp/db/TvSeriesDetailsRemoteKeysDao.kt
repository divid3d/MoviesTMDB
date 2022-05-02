package com.example.moviesapp.db

import androidx.room.*
import com.example.moviesapp.model.TvSeriesDetailsRemoteKey
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface TvSeriesDetailsRemoteKeysDao {
    @Query("SELECT * FROM TvSeriesDetailsRemoteKey WHERE language=:language LIMIT 1")
    suspend fun getRemoteKey(language: String): TvSeriesDetailsRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: TvSeriesDetailsRemoteKey)

    @Query("DELETE FROM TvSeriesDetailsRemoteKey WHERE language=:language")
    suspend fun deleteKeys(language: String)
}