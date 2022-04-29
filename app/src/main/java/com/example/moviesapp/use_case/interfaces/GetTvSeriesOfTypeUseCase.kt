package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesType
import kotlinx.coroutines.flow.Flow


interface GetTvSeriesOfTypeUseCase {
    operator fun invoke(
        type: TvSeriesType,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Presentable>>
}