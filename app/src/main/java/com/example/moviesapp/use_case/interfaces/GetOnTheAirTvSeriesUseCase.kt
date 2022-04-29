package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import kotlinx.coroutines.flow.Flow

interface GetOnTheAirTvSeriesUseCase {
    operator fun invoke(
        deviceLanguage: DeviceLanguage,
        filtered: Boolean = false
    ): Flow<PagingData<TvSeries>>
}