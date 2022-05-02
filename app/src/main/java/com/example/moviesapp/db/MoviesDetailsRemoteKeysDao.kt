package com.example.moviesapp.db

import androidx.room.*
import com.example.moviesapp.model.MovieDetailsRemoteKey
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface MoviesDetailsRemoteKeysDao {
    @Query("SELECT * FROM MovieDetailsRemoteKey WHERE language=:language LIMIT 1")
    suspend fun getRemoteKey(language: String): MovieDetailsRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: MovieDetailsRemoteKey)

    @Query("DELETE FROM MovieDetailsRemoteKey WHERE language=:language")
    suspend fun deleteKeys(language: String)
}