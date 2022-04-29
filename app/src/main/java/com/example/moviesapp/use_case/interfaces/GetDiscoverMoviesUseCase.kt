package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.ui.screens.discover.MovieFilterState
import com.example.moviesapp.ui.screens.discover.SortInfo
import kotlinx.coroutines.flow.Flow

interface GetDiscoverMoviesUseCase {
    operator fun invoke(
        sortInfo: SortInfo,
        filterState: MovieFilterState,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>>
}