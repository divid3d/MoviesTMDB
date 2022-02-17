package com.example.moviesapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

class DiscoverTvSeriesDataSource(
    private val apiHelper: TmdbApiHelper,
    private val language: String = DeviceLanguage.default.languageCode,
    private val region: String = DeviceLanguage.default.region,
    private val sortType: SortType = SortType.Popularity,
    private val sortOrder: SortOrder = SortOrder.Desc,
    private val genresParam: GenresParam = GenresParam(genres = emptyList()),
    private val watchProvidersParam: WatchProvidersParam = WatchProvidersParam(watchProviders = emptyList()),
    private val voteRange: ClosedFloatingPointRange<Float> = 0f..10f,
    private val onlyWithPosters: Boolean = false,
    private val onlyWithScore: Boolean = false,
    private val onlyWithOverview: Boolean = false,
    private val airDateRange: DateRange,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : PagingSource<Int, TvSeries>() {

    private val fromAirDate = airDateRange.from?.let { date -> DateParam(date) }
    private val toAirDate = airDateRange.to?.let { date -> DateParam(date) }
    private val sortTypeParam = sortType.toSortTypeParam(sortOrder)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvSeries> {
        return try {
            val nextPage = params.key ?: 1

            val tvSeriesResponse = apiHelper.discoverTvSeries(
                page = nextPage,
                isoCode = language,
                region = region,
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
        } catch (e: CancellationException) {
            throw  e
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            firebaseCrashlytics.recordException(e)
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