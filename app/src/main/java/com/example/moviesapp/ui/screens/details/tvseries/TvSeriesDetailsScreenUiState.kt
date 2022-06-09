package com.example.moviesapp.ui.screens.details.tvseries

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.model.WatchProviders
import com.example.moviesapp.ui.screens.details.movie.AssociatedContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class TvSeriesDetailsScreenUiState(
    val startRoute: String,
    val tvSeriesDetails: TvSeriesDetails?,
    val additionalTvSeriesDetailsInfo: AdditionalTvSeriesDetailsInfo,
    val associatedTvSeries: AssociatedTvSeries,
    val associatedContent: AssociatedContent,
    val error: String?
) {
    companion object {
        fun getDefault(startRoute: String): TvSeriesDetailsScreenUiState {
            return TvSeriesDetailsScreenUiState(
                startRoute = startRoute,
                tvSeriesDetails = null,
                additionalTvSeriesDetailsInfo = AdditionalTvSeriesDetailsInfo.default,
                associatedTvSeries = AssociatedTvSeries.default,
                associatedContent = AssociatedContent.default,
                error = null
            )
        }
    }
}

@Stable
data class AssociatedTvSeries(
    val similar: Flow<PagingData<TvSeries>>,
    val recommendations: Flow<PagingData<TvSeries>>
) {
    companion object {
        val default: AssociatedTvSeries = AssociatedTvSeries(
            similar = emptyFlow(),
            recommendations = emptyFlow()
        )
    }
}

@Stable
data class AdditionalTvSeriesDetailsInfo(
    val isFavourite: Boolean,
    val nextEpisodeRemainingDays: Long?,
    val watchProviders: WatchProviders?,
    val reviewsCount: Int
) {
    companion object {
        val default: AdditionalTvSeriesDetailsInfo = AdditionalTvSeriesDetailsInfo(
            isFavourite = false,
            nextEpisodeRemainingDays = null,
            watchProviders = null,
            reviewsCount = 0
        )
    }
}