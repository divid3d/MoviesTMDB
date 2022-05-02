package com.example.moviesapp.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviesapp.model.TvSeriesEntity
import com.example.moviesapp.model.TvSeriesEntityType
import com.example.moviesapp.other.TvSeriesEntityTypeConverters

@TypeConverters(TvSeriesEntityTypeConverters::class)
@Dao
interface TvSeriesDao {
    @Query("SELECT * FROM TvSeriesEntity WHERE type=:type AND language=:language")
    fun getAllTvSeries(
        type: TvSeriesEntityType,
        language: String
    ): PagingSource<Int, TvSeriesEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTvSeries(movies: List<TvSeriesEntity>)

    @Query("DELETE FROM TvSeriesEntity WHERE type=:type AND language=:language")
    suspend fun deleteTvSeriesOfType(type: TvSeriesEntityType, language: String)
}

