package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.DeviceRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = deviceRepository.deviceLanguage
    private val favouriteTvSeriesIds: Flow<List<Int>> =
        favouritesRepository.getFavouriteTvSeriesIds()

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)

    private val tvSeriesId: Flow<Int?> = savedStateHandle.getLiveData<Int>("tvSeriesId").asFlow()

    var similarTvSeries: Flow<PagingData<Presentable>>? = null
    var tvSeriesRecommendations: Flow<PagingData<Presentable>>? = null
    private val _tvSeriesBackdrops: MutableStateFlow<List<Image>?> = MutableStateFlow(null)
    private val _hasReviews: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val tvSeriesDetails: StateFlow<TvSeriesDetails?> =
        _tvSeriesDetails
            .onEach { tvSeriesDetails ->
                tvSeriesDetails?.let { details ->
                    recentlyBrowsedRepository.addRecentlyBrowsedTvSeries(details)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val backdrops: StateFlow<List<Image>> =
        _tvSeriesBackdrops.map { backdrops -> backdrops ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    val isFavourite: StateFlow<Boolean> = combine(
        tvSeriesId, favouriteTvSeriesIds
    ) { id, favouritesId ->
        id in favouritesId
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    val hasReviews: StateFlow<Boolean> = _hasReviews.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    init {
        viewModelScope.launch {
            tvSeriesId.collectLatest { tvSeriesId ->
                tvSeriesId?.let { id ->
                    similarTvSeries = deviceLanguage.map { deviceLanguage ->
                        tvSeriesRepository.similarTvSeries(
                            tvSeriesId = id,
                            deviceLanguage = deviceLanguage
                        ).cachedIn(viewModelScope)
                    }.flattenMerge().map { data -> data.map { tvSeries -> tvSeries } }

                    tvSeriesRecommendations = deviceLanguage.map { deviceLanguage ->
                        tvSeriesRepository.tvSeriesRecommendations(
                            tvSeriesId = id,
                            deviceLanguage = deviceLanguage
                        ).cachedIn(viewModelScope)
                    }.flattenMerge().map { data -> data.map { tvSeries -> tvSeries } }


                    deviceLanguage.collectLatest { deviceLanguage ->
                        getTvSeriesInfo(
                            tvSeriesId = id,
                            deviceLanguage = deviceLanguage
                        )
                    }

                }
            }
        }
    }

    fun onLikeClick(tvSeriesDetails: TvSeriesDetails) {
        favouritesRepository.likeTvSeries(tvSeriesDetails)
    }

    fun onUnlikeClick(tvSeriesDetails: TvSeriesDetails) {
        favouritesRepository.unlikeTvSeries(tvSeriesDetails)
    }

    private fun getTvSeriesInfo(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        getTvSeriesDetails(tvSeriesId, deviceLanguage)
        getMovieImages(tvSeriesId)
        getTvSeriesReview(tvSeriesId)
    }

    private fun getTvSeriesDetails(tvSeriesId: Int, deviceLanguage: DeviceLanguage) {
        tvSeriesRepository.getTvSeriesDetails(
            tvSeriesId = tvSeriesId,
            deviceLanguage = deviceLanguage
        ).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val tvSeriesDetails = data
                    _tvSeriesDetails.emit(tvSeriesDetails)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

    private fun getMovieImages(tvSeriesId: Int) {
        tvSeriesRepository.tvSeriesImages(tvSeriesId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val imagesResponse = data
                    _tvSeriesBackdrops.emit(imagesResponse?.backdrops)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

    private fun getTvSeriesReview(tvSeriesId: Int) {
        tvSeriesRepository.tvSeriesReview(tvSeriesId).request { response ->
            response.onSuccess {
                viewModelScope.launch {
                    val hasReviews = (data?.totalResults ?: 0) > 1

                    _hasReviews.emit(hasReviews)
                }
            }

            response.onFailure {
                onError(message)
            }

            response.onException {
                onError(message)
            }
        }
    }

}