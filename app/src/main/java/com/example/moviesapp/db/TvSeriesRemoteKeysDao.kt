package com.example.moviesapp.db

import androidx.room.*
import com.example.moviesapp.model.TvSeriesEntityType
import com.example.moviesapp.model.TvSeriesRemoteKeys
import com.example.moviesapp.other.TvSeriesEntityTypeConverters

@TypeConverters(TvSeriesEntityTypeConverters::class)
@Dao
interface TvSeriesRemoteKeysDao {
    @Query("SELECT * FROM TvSeriesRemoteKeys WHERE type=:type")
    suspend fun getRemoteKey(type: TvSeriesEntityType): TvSeriesRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: TvSeriesRemoteKeys)

    @Query("DELETE FROM TvSeriesRemoteKeys WHERE type=:type")
    suspend fun deleteRemoteKeysOfType(type: TvSeriesEntityType)
}