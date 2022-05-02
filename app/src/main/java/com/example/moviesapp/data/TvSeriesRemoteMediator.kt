package com.example.moviesapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.db.AppDatabase
import com.example.moviesapp.model.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class TvSeriesRemoteMediator(
    private val deviceLanguage: DeviceLanguage,
    private val type: TvSeriesEntityType,
    private val apiHelper: TmdbApiHelper,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, TvSeriesEntity>() {

    private val tvSeriesDao = appDatabase.tvSeriesDao()
    private val tvSeriesRemoteKeysDao = appDatabase.tvSeriesRemoteKeysDao()

    private val apiHelperMethod: suspend (Int, String, String) -> TvSeriesResponse = when (type) {
        TvSeriesEntityType.AiringToday -> apiHelper::getAiringTodayTvSeries
        TvSeriesEntityType.TopRated -> apiHelper::getTopRatedTvSeries
        TvSeriesEntityType.Trending -> apiHelper::getTrendingTvSeries
        TvSeriesEntityType.Popular -> apiHelper::getPopularTvSeries
        TvSeriesEntityType.Discover -> apiHelper::discoverTvSeries
    }

    override suspend fun initialize(): InitializeAction {
        val remoteKey = appDatabase.withTransaction {
            tvSeriesRemoteKeysDao.getRemoteKey(type)
        } ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)

        return if ((System.currentTimeMillis() - remoteKey.lastUpdated) >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TvSeriesEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = appDatabase.withTransaction {
                        tvSeriesRemoteKeysDao.getRemoteKey(type)
                    } ?: return MediatorResult.Success(true)

                    if (remoteKey.nextPage == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextPage
                }
            }

            val result = apiHelperMethod(page, deviceLanguage.languageCode, deviceLanguage.region)

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    tvSeriesDao.deleteTvSeriesOfType(type)
                    tvSeriesRemoteKeysDao.deleteRemoteKeysOfType(type)
                }

                val nextPage = if (result.tvSeries.isNotEmpty()) {
                    page + 1
                } else null

                val movieEntities = result.tvSeries.map { tvSeries ->
                    TvSeriesEntity(
                        id = tvSeries.id,
                        type = type,
                        title = tvSeries.title,
                        originalName = tvSeries.originalName,
                        posterPath = tvSeries.posterPath
                    )
                }

                tvSeriesRemoteKeysDao.insertKey(
                    TvSeriesRemoteKeys(
                        type = type,
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                tvSeriesDao.addTvSeries(movieEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = result.tvSeries.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: JsonDataException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            MediatorResult.Error(e)
        }
    }
}