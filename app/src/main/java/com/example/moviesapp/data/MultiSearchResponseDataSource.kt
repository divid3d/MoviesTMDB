package com.example.moviesapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.SearchResult

class MultiSearchResponseDataSource(
    private val apiHelper: TmdbApiHelper,
    private val query: String,
    private val includeAdult: Boolean,
    private val year: Int? = null,
    private val releaseYear: Int? = null
) : PagingSource<Int, SearchResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        return try {
            val nextPage = params.key ?: 1
            val movieResponse = apiHelper.multiSearch(
                page = nextPage,
                isoCode = "pl-PL",
                query = query,
                includeAdult = includeAdult,
                year = year,
                releaseYear = releaseYear
            )

            val currentPage = movieResponse.page
            val totalPages = movieResponse.totalPages

            LoadResult.Page(
                data = movieResponse.results.filter { result ->
                    result.mediaType in setOf(
                        MediaType.Movie,
                        MediaType.Tv
                    )
                },
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (currentPage + 1 > totalPages) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}