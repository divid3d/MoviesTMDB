package com.example.moviesapp.db

import androidx.room.*
import com.example.moviesapp.model.MovieDetailsRemoteKey
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface MoviesDetailsRemoteKeysDao {
    @Query("SELECT * FROM MovieDetailsRemoteKey LIMIT 1")
    suspend fun getRemoteKey(): MovieDetailsRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: MovieDetailsRemoteKey)

    @Query("DELETE FROM MovieDetailsRemoteKey")
    suspend fun deleteKeys()
}