package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.tv.TvSeriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTrendingTvSeriesUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) {
    operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> {
        return tvSeriesRepository.trendingTvSeries(deviceLanguage)
    }
}