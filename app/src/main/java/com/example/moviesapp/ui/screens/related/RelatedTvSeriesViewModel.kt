package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.TvSeries
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.TvSeriesRepository
import com.example.moviesapp.ui.screens.destinations.RelatedTvSeriesScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RelatedTvSeriesViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: RelatedTvSeriesScreenArgs =
        RelatedTvSeriesScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    val tvSeries: Flow<PagingData<TvSeries>> = deviceLanguage.map { deviceLanguage ->
        when (navArgs.type) {
            RelationType.Similar -> {
                tvSeriesRepository.similarTvSeries(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }

            RelationType.Recommended -> {
                tvSeriesRepository.tvSeriesRecommendations(
                    tvSeriesId = navArgs.tvSeriesId,
                    deviceLanguage = deviceLanguage
                )
            }
        }
    }.flattenMerge().cachedIn(viewModelScope)
}