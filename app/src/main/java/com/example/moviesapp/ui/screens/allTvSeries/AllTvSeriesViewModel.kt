package com.example.moviesapp.ui.screens.allTvSeries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.*
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.other.getImageUrl
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class AllTvSeriesViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val config = configRepository.config

    private val tvSeriesType: Flow<TvSeriesType> = savedStateHandle
        .getLiveData("tvSeriesType", TvSeriesType.Popular.name)
        .asFlow().map { value ->
            TvSeriesType.valueOf(value)
        }

    private val topRated: Flow<PagingData<Presentable>> =
        tvSeriesRepository.topRatedTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }

    private val airingToday: Flow<PagingData<Presentable>> =
        tvSeriesRepository.airingTodayTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }
    private val popular: Flow<PagingData<Presentable>> =
        tvSeriesRepository.popularTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }
    private val favourites: Flow<PagingData<Presentable>> =
        favouritesRepository.favouritesTvSeries().combine(config) { pagingData, config ->
            pagingData.map { tvSeries -> tvSeries.appendUrls(config) }
        }

    val tvSeries: Flow<PagingData<Presentable>> = combine(
        tvSeriesType,
        topRated,
        airingToday,
        popular,
        favourites
    ) { type, topRated, airingToday, popular, favourites ->
        when (type) {
            TvSeriesType.TopRated -> topRated
            TvSeriesType.AiringToday -> airingToday
            TvSeriesType.Popular -> popular
            TvSeriesType.Favourite -> favourites
        }
    }

    private fun TvSeries.appendUrls(
        config: Config?
    ): TvSeries {
        val moviePosterUrl = config?.getImageUrl(posterPath)
        val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

        return copy(
            posterUrl = moviePosterUrl,
            backdropUrl = movieBackdropUrl
        )
    }

    private fun TvSeriesFavourite.appendUrls(
        config: Config?
    ): TvSeriesFavourite {
        val moviePosterUrl = config?.getImageUrl(posterPath)
        val movieBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

        return copy(
            posterUrl = moviePosterUrl,
            backdropUrl = movieBackdropUrl
        )
    }

}