package com.example.moviesapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviesapp.model.MovieEntity
import com.example.moviesapp.model.MovieEntityType
import com.example.moviesapp.other.Converters

@TypeConverters(Converters::class)
@Dao
interface MoviesDao {
    @Query("SELECT * FROM MovieEntity WHERE type=:type")
    fun getAllMovies(type: MovieEntityType): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM MovieEntity WHERE type=:type")
    suspend fun deleteMoviesOfType(type: MovieEntityType)
}

