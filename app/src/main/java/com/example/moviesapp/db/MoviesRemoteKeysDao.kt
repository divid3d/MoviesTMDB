package com.example.moviesapp.db

import androidx.room.*
import com.example.moviesapp.model.MovieEntityType
import com.example.moviesapp.model.MoviesRemoteKeys
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface MoviesRemoteKeysDao {
    @Query("SELECT * FROM MoviesRemoteKeys WHERE type=:type")
    suspend fun getRemoteKey(type: MovieEntityType): MoviesRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: MoviesRemoteKeys)

    @Query("DELETE FROM MoviesRemoteKeys WHERE type=:type")
    suspend fun deleteRemoteKeysOfType(type: MovieEntityType)
}