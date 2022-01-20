package com.example.moviesapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.TvSeriesFavourite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesTvSeriesDao {

    @Query("SELECT * FROM TvSeriesFavourite ORDER BY added_date DESC ")
    fun favouriteTvSeries(): DataSource.Factory<Int, TvSeriesFavourite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likeTvSeries(vararg tvSeriesDetails: TvSeriesFavourite)

    @Query("DELETE FROM TvSeriesFavourite WHERE id = :tvSeriesId")
    suspend fun unlikeTvSeries(tvSeriesId: Int)

    @Query("SELECT id FROM TvSeriesFavourite")
    fun favouriteTvSeriesIds(): Flow<List<Int>>

    @Query("SELECT COUNT(id) FROM TvSeriesFavourite")
    fun favouriteTvSeriesCount(): Flow<Int>

}