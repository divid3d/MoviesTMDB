package com.example.moviesapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.db.AppDatabase
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeriesDetailEntity
import com.example.moviesapp.model.TvSeriesDetailsRemoteKey
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class TvSeriesDetailsRemoteMediator(
    private val deviceLanguage: DeviceLanguage,
    private val apiHelper: TmdbApiHelper,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, TvSeriesDetailEntity>() {

    private val tvSeriesDetailsDao = appDatabase.tvSeriesDetailsDao()
    private val tvSeriesDetailsRemoteKeysDao = appDatabase.tvSeriesDetailsRemoteKeys()

    override suspend fun initialize(): InitializeAction {
        val remoteKey = appDatabase.withTransaction {
            tvSeriesDetailsRemoteKeysDao.getRemoteKey()
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
        state: PagingState<Int, TvSeriesDetailEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = appDatabase.withTransaction {
                        tvSeriesDetailsRemoteKeysDao.getRemoteKey()
                    } ?: return MediatorResult.Success(true)

                    if (remoteKey.nextPage == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextPage
                }
            }

            val result = apiHelper.getOnTheAirTvSeries(
                page = page,
                isoCode = deviceLanguage.languageCode,
                region = deviceLanguage.region
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    tvSeriesDetailsDao.deleteAllTvSeries()
                    tvSeriesDetailsRemoteKeysDao.deleteKeys()
                }

                val nextPage = if (result.tvSeries.isNotEmpty()) {
                    page + 1
                } else null

                val tvSeriesEntities = result.tvSeries.map { tvSeries ->
                    TvSeriesDetailEntity(
                        id = tvSeries.id,
                        title = tvSeries.title,
                        originalTitle = tvSeries.originalName,
                        posterPath = tvSeries.posterPath,
                        backdropPath = tvSeries.backdropPath,
                        overview = tvSeries.overview,
                        adult = tvSeries.adult,
                        voteAverage = tvSeries.voteAverage,
                        voteCount = tvSeries.voteCount
                    )
                }

                tvSeriesDetailsRemoteKeysDao.insertKey(
                    TvSeriesDetailsRemoteKey(
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                tvSeriesDetailsDao.addTvSeries(tvSeriesEntities)
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