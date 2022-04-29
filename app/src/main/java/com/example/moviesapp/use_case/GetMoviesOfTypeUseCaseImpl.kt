package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.use_case.interfaces.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviesOfTypeUseCaseImpl @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getFavouritesMoviesUseCaseImpl: GetFavouritesMoviesUseCase,
    private val getRecentlyBrowsedMoviesUseCase: GetRecentlyBrowsedMoviesUseCase,
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : GetMoviesOfTypeUseCase {
    override operator fun invoke(
        type: MovieType,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Presentable>> {
        return when (type) {
            MovieType.NowPlaying -> getNowPlayingMoviesUseCase(deviceLanguage)
            MovieType.TopRated -> getTopRatedMoviesUseCase(deviceLanguage)
            MovieType.Upcoming -> getUpcomingMoviesUseCase(deviceLanguage)
            MovieType.Favourite -> getFavouritesMoviesUseCaseImpl()
            MovieType.RecentlyBrowsed -> getRecentlyBrowsedMoviesUseCase()
            MovieType.Trending -> getTrendingMoviesUseCase(deviceLanguage)
        }.mapLatest { data -> data.map { it } }
    }
}