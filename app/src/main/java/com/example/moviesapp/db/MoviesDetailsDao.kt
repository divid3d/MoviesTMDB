package com.example.moviesapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviesapp.model.MovieDetailEntity
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface MoviesDetailsDao {
    @Query("SELECT * FROM MovieDetailEntity WHERE language=:language")
    fun getAllMovies(language: String): PagingSource<Int, MovieDetailEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovies(movies: List<MovieDetailEntity>)

    @Query("DELETE FROM MovieDetailEntity WHERE language=:language")
    suspend fun deleteMovieDetails(language: String)
}

