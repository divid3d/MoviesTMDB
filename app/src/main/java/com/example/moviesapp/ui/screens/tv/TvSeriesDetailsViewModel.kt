package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.other.appendUrl
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.other.getImageUrl
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
import com.example.moviesapp.repository.RecentlyBrowsedRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val favouritesRepository: FavouritesRepository,
    private val recentlyBrowsedRepository: RecentlyBrowsedRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val config: StateFlow<Config?> = configRepository.config
    private val favouriteTvSeriesIds: Flow<List<Int>> =
        favouritesRepository.getFavouriteTvSeriesIds()

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)

    private val tvSeriesId: Flow<Int?> = savedStateHandle.getLiveData<Int>("tvSeriesId").asFlow()

    var similarTvSeries: Flow<PagingData<Presentable>>? = null
    var tvSeriesRecommendations: Flow<PagingData<Presentable>>? = null
    private val _tvSeriesBackdrops: MutableStateFlow<List<Backdrop>?> = MutableStateFlow(null)

    val tvSeriesDetails: StateFlow<TvSeriesDetails?> = combine(
        _tvSeriesDetails, config
    ) { details, config ->
        val posterUrl = config?.getImageUrl(details?.posterPath)
        val backdropUrl = config?.getImageUrl(details?.backdropPath)

        details?.copy(
            creators = details.creators.map { creator -> creator.appendUrls(config) },
            lastEpisodeToAir = details.lastEpisodeToAir.appendUrls(config),
            seasons = details.seasons.map { season -> season.appendUrl(config) },
            networks = details.networks.map { network -> network.appendUrls(config) },
            posterUrl = posterUrl,
            backdropUrl = backdropUrl
        )
    }
        .onEach { tvSeriesDetails ->
            tvSeriesDetails?.let { details ->
                recentlyBrowsedRepository.addRecentlyBrowsedTvSeries(details)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val backdrops: StateFlow<List<Backdrop>> = combine(
        _tvSeriesBackdrops, config
    ) { backdrops, config ->
        backdrops?.map { backdrop -> backdrop.appendUrls(config) } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    val isFavourite: StateFlow<Boolean> = combine(
        tvSeriesId, favouriteTvSeriesIds
    ) { id, favouritesId ->
        id in favouritesId
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), false)

    private val _selectedSeason: MutableStateFlow<TvSeasonsResponse?> = MutableStateFlow(null)
    val selectedSeason: StateFlow<TvSeasonsResponse?> =
        _selectedSeason.combine(config) { season, config ->
            season?.appendUrl(config)?.copy(
                episodes = season.episodes.map { episode -> episode.appendUrls(config) }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    init {
        viewModelScope.launch {
            tvSeriesId.collectLatest { tvSeriesId ->
                tvSeriesId?.let { id ->
                    similarTvSeries = tvSeriesRepository.similarTvSeries(id)
                        .cachedIn(viewModelScope)
                        .combine(config) { moviePagingData, config ->
                            moviePagingData
                                .filter { tvSeries -> tvSeries.id != id }
                                .map { tvSeries -> tvSeries.appendUrls(config) }
                        }

                    tvSeriesRecommendations = tvSeriesRepository.tvSeriesRecommendations(id)
                        .cachedIn(viewModelScope)
                        .combine(config) { moviePagingData, config ->
                            moviePagingData
                                .filter { tvSeries -> tvSeries.id != id }
                                .map { tvSeries -> tvSeries.appendUrls(config) }
                        }

                    getTvSeriesInfo(id)
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

    private fun getTvSeriesInfo(tvSeriesId: Int) {
        getTvSeriesDetails(tvSeriesId)
        getMovieImages(tvSeriesId)
    }

    private fun getTvSeriesDetails(tvSeriesId: Int) {
        tvSeriesRepository.getTvSeriesDetails(tvSeriesId).request { response ->
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

    private fun getTvSeason(seasonNumber: Int) {
        viewModelScope.launch {
            val selectedSeasonNumber = _selectedSeason.value?.seasonNumber

            if (seasonNumber != selectedSeasonNumber) {
                tvSeriesId.collectLatest { id ->
                    id?.let {
                        tvSeriesRepository.getTvSeason(
                            tvSeriesId = it,
                            seasonNumber = seasonNumber
                        ).request { response ->
                            response.onSuccess {
                                viewModelScope.launch {
                                    val season = data
                                    _selectedSeason.emit(season)
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
            } else {
                _selectedSeason.emit(null)
            }
        }
    }

}