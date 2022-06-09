package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.GenresParam
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.WatchProvidersParam
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.ui.screens.discover.movies.SortInfo
import com.example.moviesapp.ui.screens.discover.tvseries.TvSeriesFilterState
import com.example.moviesapp.use_case.interfaces.GetDiscoverTvSeriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoverTvSeriesUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetDiscoverTvSeriesUseCase {
    override operator fun invoke(
        sortInfo: SortInfo,
        filterState: TvSeriesFilterState,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<TvSeries>> {
        return tvSeriesRepository.discoverTvSeries(
            deviceLanguage = deviceLanguage,
            sortType = sortInfo.sortType,
            sortOrder = sortInfo.sortOrder,
            genresParam = GenresParam(filterState.selectedGenres),
            watchProvidersParam = WatchProvidersParam(filterState.selectedWatchProviders),
            voteRange = filterState.voteRange.current,
            onlyWithPosters = filterState.showOnlyWithPoster,
            onlyWithScore = filterState.showOnlyWithScore,
            onlyWithOverview = filterState.showOnlyWithOverview,
            airDateRange = filterState.airDateRange
        )
    }
}