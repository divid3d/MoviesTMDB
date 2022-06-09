package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.ui.screens.discover.movies.SortInfo
import com.example.moviesapp.ui.screens.discover.tvseries.TvSeriesFilterState
import kotlinx.coroutines.flow.Flow

interface GetDiscoverTvSeriesUseCase {
    operator fun invoke(
        sortInfo: SortInfo,
        filterState: TvSeriesFilterState,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<TvSeries>>
}