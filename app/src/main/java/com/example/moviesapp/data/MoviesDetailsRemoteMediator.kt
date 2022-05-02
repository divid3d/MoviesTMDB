package com.example.moviesapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.db.AppDatabase
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieDetailEntity
import com.example.moviesapp.model.MovieDetailsRemoteKey
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MoviesDetailsRemoteMediator(
    private val deviceLanguage: DeviceLanguage,
    private val apiHelper: TmdbApiHelper,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, MovieDetailEntity>() {

    private val movieDetailsDao = appDatabase.moviesDetailsDao()
    private val movieDetailsRemoteKeysDao = appDatabase.moviesDetailsRemoteKeys()

    override suspend fun initialize(): InitializeAction {
        val remoteKey = appDatabase.withTransaction {
            movieDetailsRemoteKeysDao.getRemoteKey()
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
        state: PagingState<Int, MovieDetailEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = appDatabase.withTransaction {
                        movieDetailsRemoteKeysDao.getRemoteKey()
                    } ?: return MediatorResult.Success(true)

                    if (remoteKey.nextPage == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextPage
                }
            }

            val result = apiHelper.getNowPlayingMovies(
                page = page,
                isoCode = deviceLanguage.languageCode,
                region = deviceLanguage.region
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDetailsDao.deleteMovieDetails()
                    movieDetailsRemoteKeysDao.deleteKeys()
                }

                val nextPage = if (result.movies.isNotEmpty()) {
                    page + 1
                } else null

                val movieDetailsEntities = result.movies.map { movie ->
                    MovieDetailEntity(
                        id = movie.id,
                        title = movie.title,
                        originalTitle = movie.originalTitle,
                        posterPath = movie.posterPath,
                        backdropPath = movie.backdropPath,
                        overview = movie.overview,
                        adult = movie.adult,
                        voteAverage = movie.voteAverage,
                        voteCount = movie.voteCount
                    )
                }

                movieDetailsRemoteKeysDao.insertKey(
                    MovieDetailsRemoteKey(
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                movieDetailsDao.addMovies(movieDetailsEntities)
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