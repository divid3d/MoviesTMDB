package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetDiscoverAllTvSeriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoverAllTvSeriesUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetDiscoverAllTvSeriesUseCase {
    override operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> {
        return tvSeriesRepository.discoverTvSeries(deviceLanguage)
    }
}