package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.tv.TvSeriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRelatedTvSeriesOfTypeUseCase @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository
){
    operator fun invoke(
        tvSeriesId: Int,
        type: RelationType,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<TvSeries>>{
        return when (type) {
            RelationType.Similar -> {
                tvSeriesRepository.similarTvSeries(
                    tvSeriesId = tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }

            RelationType.Recommended -> {
                tvSeriesRepository.tvSeriesRecommendations(
                    tvSeriesId = tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }
        }
    }
}