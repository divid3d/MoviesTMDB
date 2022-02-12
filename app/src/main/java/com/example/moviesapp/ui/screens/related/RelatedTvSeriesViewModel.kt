package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.TvSeriesRelationInfo
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelatedTvSeriesViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var tvSeries: Flow<PagingData<Presentable>>? = null

    private val movieRelationType: Flow<TvSeriesRelationInfo?> = savedStateHandle
        .getLiveData("tvSeriesRelationInfo", null).asFlow()

    init {
        viewModelScope.launch {
            movieRelationType.collectLatest { relationType ->
                when (relationType?.type) {
                    RelationType.Similar -> {
                        val id = relationType.tvSeriesId

                        tvSeries = tvSeriesRepository.similarTvSeries(id)
                            .cachedIn(viewModelScope)
                            .map { data -> data.map { movie -> movie } }
                    }

                    RelationType.Recommended -> {
                        val id = relationType.tvSeriesId

                        tvSeries = tvSeriesRepository.tvSeriesRecommendations(id)
                            .cachedIn(viewModelScope)
                            .map { data -> data.map { movie -> movie } }
                    }
                    else -> Unit
                }
            }
        }
    }
}