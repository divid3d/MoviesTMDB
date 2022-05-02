package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Presentable
import kotlinx.coroutines.flow.Flow

interface GetAiringTodayTvSeriesUseCase {
    operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<Presentable>>
}