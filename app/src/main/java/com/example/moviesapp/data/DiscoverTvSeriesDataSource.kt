package com.example.moviesapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException

class DiscoverTvSeriesDataSource(
    private val apiHelper: TmdbApiHelper,
    private val deviceLanguage: DeviceLanguage,
    private val sortType: SortType = SortType.Popularity,
    private val sortOrder: SortOrder = SortOrder.Desc,
    private val genresParam: GenresParam = GenresParam(genres = emptyList()),
    private val watchProvidersParam: WatchProvidersParam = WatchProvidersParam(watchProviders = emptyList()),
    private val voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
    private val onlyWithPosters: Boolean = false,
    private val onlyWithScore: Boolean = false,
    private val onlyWithOverview: Boolean = false,
    private val airDateRange: DateRange
) : PagingSource<Int, TvSeries>() {

    private val fromAirDate = airDateRange.from?.let (::DateParam)
    private val toAirDate = airDateRange.to?.let (::DateParam)
    private val sortTypeParam = sortType.toSortTypeParam(sortOrder)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvSeries> {
        return try {
            val nextPage = params.key ?: 1

            val tvSeriesResponse = apiHelper.discoverTvSeries(
                page = nextPage,
                isoCode = deviceLanguage.languageCode,
                region = deviceLanguage.region,
                sortTypeParam = sortTypeParam,
                genresParam = genresParam,
                watchProvidersParam = watchProvidersParam,
                voteRange = voteRange,
                fromAirDate = fromAirDate,
                toAirDate = toAirDate
            )

            val currentPage = tvSeriesResponse.page
            val totalPages = tvSeriesResponse.totalPages

            LoadResult.Page(
                data = tvSeriesResponse.tvSeries
                    .filter { tvSeries ->
                        if (onlyWithPosters) !tvSeries.posterPath.isNullOrEmpty() else true
                    }
                    .filter { tvSeries ->
                        if (onlyWithScore) tvSeries.voteCount > 0 && tvSeries.voteAverage > 0f else true
                    }
                    .filter { tvSeries ->
                        if (onlyWithOverview) tvSeries.overview.isNotBlank() else true
                    },
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (currentPage + 1 > totalPages) null else currentPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: JsonDataException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TvSeries>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}