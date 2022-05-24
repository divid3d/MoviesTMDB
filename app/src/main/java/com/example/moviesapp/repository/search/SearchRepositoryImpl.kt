package com.example.moviesapp.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.MultiSearchResponseDataSource
import com.example.moviesapp.db.SearchQueryDao
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.model.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val externalScope: CoroutineScope,
    private val apiHelper: TmdbApiHelper,
    private val searchQueryDao: SearchQueryDao
) : SearchRepository {
    override fun multiSearch(
        query: String,
        includeAdult: Boolean,
        year: Int?,
        releaseYear: Int?,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<SearchResult>> =
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            MultiSearchResponseDataSource(
                apiHelper = apiHelper,
                query = query,
                includeAdult = includeAdult,
                year = year,
                releaseYear = releaseYear,
                language = deviceLanguage.languageCode
            )
        }.flow.flowOn(defaultDispatcher)

    override suspend fun searchQueries(query: String): List<String> =
        searchQueryDao.searchQueries(query)

    override fun addSearchQuery(searchQuery: SearchQuery) {
        externalScope.launch(defaultDispatcher) {
            searchQueryDao.addQuery(searchQuery)
        }
    }

}