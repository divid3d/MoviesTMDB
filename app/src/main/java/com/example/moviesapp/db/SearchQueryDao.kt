package com.example.moviesapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.model.SearchQuery

@Dao
interface SearchQueryDao {

    @Query("SELECT query FROM SearchQuery WHERE query LIKE '%' || :query || '%' ORDER BY last_use_date DESC LIMIT 3")
    suspend fun searchQueries(query: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuery(searchQuery: SearchQuery)

}