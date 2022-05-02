package com.example.moviesapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviesapp.model.TvSeriesDetailEntity
import com.example.moviesapp.other.MovieEntityTypeConverters

@TypeConverters(MovieEntityTypeConverters::class)
@Dao
interface TvSeriesDetailsDao {
    @Query("SELECT * FROM TvSeriesDetailEntity")
    fun getAllTvSeries(): PagingSource<Int, TvSeriesDetailEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTvSeries(movies: List<TvSeriesDetailEntity>)

    @Query("DELETE FROM TvSeriesDetailEntity")
    suspend fun deleteAllTvSeries()
}

