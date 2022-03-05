package com.example.moviesapp.repository.search

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun multiSearch(
        query: String,
        includeAdult: Boolean = false,
        year: Int? = null,
        releaseYear: Int? = null,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Flow<PagingData<SearchResult>>

    suspend fun searchQueries(query: String): List<String>

    fun addSearchQuery(searchQuery: SearchQuery)
}