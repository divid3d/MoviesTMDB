package com.example.moviesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.MultiSearchResponseDataSource
import com.example.moviesapp.model.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper
) {
    fun multiSearch(
        query: String,
        includeAdult: Boolean = false,
        year: Int? = null,
        releaseYear: Int? = null
    ): Flow<PagingData<SearchResult>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MultiSearchResponseDataSource(
                apiHelper = apiHelper,
                query = query,
                includeAdult = includeAdult,
                year = year,
                releaseYear = releaseYear
            )
        }.flow.flowOn(defaultDispatcher)
}