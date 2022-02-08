package com.example.moviesapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.*
import retrofit2.HttpException
import java.io.IOException

class DiscoverMoviesDataSource(
    private val apiHelper: TmdbApiHelper,
    private val sortType: SortType = SortType.Popularity,
    private val sortOrder: SortOrder = SortOrder.Desc,
    private val genresParam: GenresParam = GenresParam(genres = emptyList()),
    private val voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
    private val onlyWithPosters: Boolean = false,
    private val releaseDateRange: ReleaseDateRange
) : PagingSource<Int, Movie>() {

    private val fromReleaseDate = releaseDateRange.from?.let { date -> DateParam(date) }
    private val toReleaseDate = releaseDateRange.to?.let { date -> DateParam(date) }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val sortTypeParam = sortType.toSortTypeParam(sortOrder)

            val movieResponse = apiHelper.discoverMovies(
                page = nextPage,
                sortTypeParam = sortTypeParam,
                genresParam = genresParam,
                voteRange = voteRange,
                fromReleaseDate = fromReleaseDate,
                toReleaseDate = toReleaseDate
            )

            val currentPage = movieResponse.page
            val totalPages = movieResponse.totalPages

            LoadResult.Page(
                data = movieResponse.movies.filter { movie ->
                    if (onlyWithPosters) !movie.posterPath.isNullOrEmpty() else true
                },
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (currentPage + 1 > totalPages) null else currentPage + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

}