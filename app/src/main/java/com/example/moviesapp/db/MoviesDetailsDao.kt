package com.example.moviesapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviesapp.model.MovieDetailEntity
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface MoviesDetailsDao {
    @Query("SELECT * FROM MovieDetailEntity")
    fun getAllMovies(): PagingSource<Int, MovieDetailEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovies(movies: List<MovieDetailEntity>)

    @Query("DELETE FROM MovieDetailEntity")
    suspend fun deleteMovieDetails()
}

