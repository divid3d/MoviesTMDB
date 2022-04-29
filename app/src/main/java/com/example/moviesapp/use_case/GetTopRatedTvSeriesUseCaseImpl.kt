package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.interfaces.GetTopRatedTvSeriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopRatedTvSeriesUseCaseImpl @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
) : GetTopRatedTvSeriesUseCase {
    override operator fun invoke(deviceLanguage: DeviceLanguage): Flow<PagingData<TvSeries>> {
        return tvSeriesRepository.topRatedTvSeries(deviceLanguage)
    }
}