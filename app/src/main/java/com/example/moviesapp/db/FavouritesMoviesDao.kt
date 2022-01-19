package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.MovieFavourite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesMoviesDao {

    @Query("SELECT * FROM MovieFavourite")
    fun favouriteMovies(): DataSource.Factory<Int, MovieFavourite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likeMovie(vararg movieDetails: MovieFavourite)

    @Query("DELETE FROM MovieFavourite WHERE id = :movieId")
    suspend fun unlikeMovie(movieId: Int)

    @Query("SELECT id FROM MovieFavourite")
    fun favouriteMoviesIds(): Flow<List<Int>>

    @Query("SELECT COUNT(id) FROM MovieFavourite")
    fun favouriteMoviesCount(): Flow<Int>

}