package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetMoviesOfTypeUseCase @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getFavouritesMoviesUseCase: GetFavouritesMoviesUseCase,
    private val getRecentlyBrowsedMoviesUseCase: GetRecentlyBrowsedMoviesUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) {
    operator fun invoke(
        type: MovieType,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Presentable>> {
        return when (type) {
            MovieType.NowPlaying -> getNowPlayingMoviesUseCase(deviceLanguage)
            MovieType.TopRated -> getTopRatedMoviesUseCase(deviceLanguage)
            MovieType.Upcoming -> getUpcomingMoviesUseCase(deviceLanguage)
            MovieType.Favourite -> getFavouritesMoviesUseCase()
            MovieType.RecentlyBrowsed -> getRecentlyBrowsedMoviesUseCase()
            MovieType.Trending -> getTrendingMoviesUseCase(deviceLanguage)
        }.mapLatest { data -> data.map { it } }
    }
}