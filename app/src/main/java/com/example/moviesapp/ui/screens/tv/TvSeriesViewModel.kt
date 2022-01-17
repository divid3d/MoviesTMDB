package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.other.getImageUrl
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class TvSeriesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository
) : ViewModel() {

    private val config = configRepository.config

    val onTheAirTvSeriesPagingDataFlow: Flow<PagingData<Presentable>> =
        tvSeriesRepository.onTheAirTvSeries().combine(config) { pagingData, config ->
            pagingData
                .filter { tvSeries ->
                    tvSeries.run {
                        !backdropPath.isNullOrEmpty() && !posterPath.isNullOrEmpty() && title.isNotEmpty() && overview.isNotEmpty()
                    }
                }
                .map { tvSeries -> tvSeries.appendUrls(config) }
        }

    val popularTvSeriesPagingDataFlow: Flow<PagingData<Presentable>> =
        tvSeriesRepository.popularTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }

    val topRatedTvSeriesPagingDataFlow: Flow<PagingData<Presentable>> =
        tvSeriesRepository.topRatedTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }

    val airingTodayTvSeriesPagingDataFlow: Flow<PagingData<Presentable>> =
        tvSeriesRepository.airingTodayTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }

    private fun TvSeries.appendUrls(
        config: Config?
    ): TvSeries {
        val tvSeriesPosterUrl = config?.getImageUrl(posterPath)
        val tvSeriesBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

        return copy(
            posterUrl = tvSeriesPosterUrl,
            backdropUrl = tvSeriesBackdropUrl
        )
    }

}