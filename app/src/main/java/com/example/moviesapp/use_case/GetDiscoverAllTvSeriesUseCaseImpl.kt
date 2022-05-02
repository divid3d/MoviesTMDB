package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetDiscoverAllTvSeriesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetDiscoverAllTvSeriesUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetDiscoverAllTvSeriesUseCase {
    override operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<Presentable>> {
        return tvSeriesRepository.discoverTvSeries(deviceLanguage)
            .mapLatest { data ->
                data.map { it }
            }
    }
}