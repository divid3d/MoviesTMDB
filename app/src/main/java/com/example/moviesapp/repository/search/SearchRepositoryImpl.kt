package com.example.moviesapp.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.MultiSearchResponseDataSource
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiHelper: TmdbApiHelper
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
}