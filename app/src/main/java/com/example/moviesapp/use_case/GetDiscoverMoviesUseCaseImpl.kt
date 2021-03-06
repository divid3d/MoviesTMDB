package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.GenresParam
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.WatchProvidersParam
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.ui.screens.discover.movies.MovieFilterState
import com.example.moviesapp.ui.screens.discover.movies.SortInfo
import com.example.moviesapp.use_case.interfaces.GetDiscoverMoviesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoverMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetDiscoverMoviesUseCase {
    override operator fun invoke(
        sortInfo: SortInfo,
        filterState: MovieFilterState,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> {
        return movieRepository.discoverMovies(
            deviceLanguage = deviceLanguage,
            sortType = sortInfo.sortType,
            sortOrder = sortInfo.sortOrder,
            genresParam = GenresParam(filterState.selectedGenres),
            watchProvidersParam = WatchProvidersParam(filterState.selectedWatchProviders),
            voteRange = filterState.voteRange.current,
            onlyWithPosters = filterState.showOnlyWithPoster,
            onlyWithScore = filterState.showOnlyWithScore,
            onlyWithOverview = filterState.showOnlyWithOverview,
            releaseDateRange = filterState.releaseDateRange
        )
    }
}