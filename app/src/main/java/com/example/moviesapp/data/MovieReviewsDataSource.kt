package com.example.moviesapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.model.MovieReviewsResponse
import com.example.moviesapp.model.Review

class MovieReviewsDataSource(
    private val movieId: Int,
    private inline val apiHelperMethod: suspend (Int, Int) -> MovieReviewsResponse
) : PagingSource<Int, Review>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val nextPage = params.key ?: 1
            val reviewsResponse = apiHelperMethod(movieId, nextPage)

            val currentPage = reviewsResponse.page
            val totalPages = reviewsResponse.totalPages

            LoadResult.Page(
                data = reviewsResponse.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (currentPage + 1 > totalPages) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}