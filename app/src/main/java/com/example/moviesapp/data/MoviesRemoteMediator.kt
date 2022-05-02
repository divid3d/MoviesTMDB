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
class MoviesRemoteMediator(
    private val deviceLanguage: DeviceLanguage,
    private val type: MovieEntityType,
    private val apiHelper: TmdbApiHelper,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private val movieCacheDao = appDatabase.movieDao()
    private val remoteKeysDao = appDatabase.moviesRemoteKeysDao()

    private val apiHelperMethod: suspend (Int, String, String) -> MoviesResponse = when (type) {
        MovieEntityType.TopRated -> apiHelper::getTopRatedMovies
        MovieEntityType.Discover -> apiHelper::discoverMovies
        MovieEntityType.Trending -> apiHelper::getTrendingMovies
        MovieEntityType.Upcoming -> apiHelper::getUpcomingMovies
        MovieEntityType.Popular -> apiHelper::getPopularMovies
    }

    override suspend fun initialize(): InitializeAction {
        val remoteKey = appDatabase.withTransaction {
            remoteKeysDao.getRemoteKey(
                type = type,
                language = deviceLanguage.languageCode
            )
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
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = appDatabase.withTransaction {
                        remoteKeysDao.getRemoteKey(
                            type = type,
                            language = deviceLanguage.languageCode
                        )
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
                    movieCacheDao.deleteMoviesOfType(
                        type = type,
                        language = deviceLanguage.languageCode
                    )
                    remoteKeysDao.deleteRemoteKeysOfType(
                        type = type,
                        language = deviceLanguage.languageCode
                    )
                }

                val nextPage = if (result.movies.isNotEmpty()) {
                    page + 1
                } else null

                val movieEntities = result.movies.map { movie ->
                    MovieEntity(
                        id = movie.id,
                        type = type,
                        title = movie.title,
                        originalTitle = movie.originalTitle,
                        posterPath = movie.posterPath,
                        language = deviceLanguage.languageCode
                    )
                }

                remoteKeysDao.insertKey(
                    MoviesRemoteKeys(
                        language = deviceLanguage.languageCode,
                        type = type,
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                movieCacheDao.addMovies(movieEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = result.movies.isEmpty()
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