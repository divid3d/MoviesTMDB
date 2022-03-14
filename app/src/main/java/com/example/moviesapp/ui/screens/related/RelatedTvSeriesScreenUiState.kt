package com.example.moviesapp.ui.screens.related

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class RelatedTvSeriesScreenUiState(
    val relationType: RelationType,
    val tvSeries: Flow<PagingData<TvSeries>>,
    val startRoute: String
) {
    companion object {
        fun getDefault(relationType: RelationType): RelatedTvSeriesScreenUiState {
            return RelatedTvSeriesScreenUiState(
                relationType = relationType,
                tvSeries = emptyFlow(),
                startRoute = MoviesScreenDestination.route
            )
        }
    }
}