package com.example.moviesapp.use_case

import androidx.paging.PagingData
import androidx.paging.filter
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.tv.TvSeriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class GetOnTheAirTvSeriesUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
){
    operator fun invoke(deviceLanguage: DeviceLanguage, filter: Boolean = false): Flow<PagingData<TvSeries>> {
        return tvSeriesRepository.onTheAirTvSeries(deviceLanguage).apply {
            if(filter){
                mapLatest { data -> data.filterCompleteInfo() }
            }
        }
    }

    private fun PagingData<TvSeries>.filterCompleteInfo(): PagingData<TvSeries> {
        return filter { tvSeries ->
            tvSeries.run {
                !backdropPath.isNullOrEmpty() &&
                        !posterPath.isNullOrEmpty() &&
                        title.isNotEmpty() &&
                        overview.isNotEmpty()
            }
        }
    }
}