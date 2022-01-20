package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.Episode
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.other.appendUrl
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.other.getImageUrl
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.FavouritesRepository
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val config: StateFlow<Config?> = configRepository.config
    private val favouriteTvSeriesIds: Flow<List<Int>> =
        favouritesRepository.getFavouriteTvSeriesIds()

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)

    private val tvSeriesId: Flow<Int?> = savedStateHandle.getLiveData<Int>("tvSeriesId").asFlow()

    var similarTvSeries: Flow<PagingData<Presentable>>? = null
    var tvSeriesRecommendations: Flow<PagingData<Presentable>>? = null

    val tvSeriesDetails: StateFlow<TvSeriesDetails?> = combine(
        _tvSeriesDetails, config, favouriteTvSeriesIds
    ) { details, config, favouriteIds ->
        val posterUrl = config?.getImageUrl(details?.posterPath)
        val backdropUrl = config?.getImageUrl(details?.backdropPath)

        details?.copy(
            creators = details.creators.map { creator -> creator.appendUrls(config) },
            lastEpisodeToAir = details.lastEpisodeToAir.appendUrls(config),
            seasons = details.seasons.map { season -> season.appendUrl(config) },
            networks = details.networks.map { network -> network.appendUrls(config) },
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
            isFavourite = details.id in favouriteIds
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val seasonNumbers: StateFlow<List<Int>> = _tvSeriesDetails.map { details ->
        details?.seasons?.map { season -> season.seasonNumber } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    private val _selectedSeasonNumber: MutableStateFlow<Int?> = MutableStateFlow(null)
    val selectedSeasonNumber: StateFlow<Int?> = _selectedSeasonNumber.asStateFlow()

    private val _episodes: MutableStateFlow<List<Episode>> =
        MutableStateFlow(emptyList())
    val episodes: StateFlow<List<Episode>> = _episodes.combine(config) { episodes, config ->
        episodes.map { episode -> episode.appendUrls(config) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    init {
        viewModelScope.launch {
            tvSeriesId.collectLatest { tvSeriesId ->
                tvSeriesId?.let { id ->
                    similarTvSeries = tvSeriesRepository.similarTvSeries(id)
                        .combine(config) { moviePagingData, config ->
                            moviePagingData.map { tvSeries ->
                                tvSeries.appendUrls(config)
                            }
                        }

                    tvSeriesRecommendations = tvSeriesRepository.tvSeriesRecommendations(id)
                        .combine(config) { moviePagingData, config ->
                            moviePagingData.map { tvSeries ->
                                tvSeries.appendUrls(config)
                            }
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
    }

    private fun getTvSeriesDetails(tvSeriesId: Int) {
        viewModelScope.launch {
            val tvSeriesDetails = tvSeriesRepository.getTvSeriesDetails(tvSeriesId)
            _tvSeriesDetails.emit(tvSeriesDetails)
        }
    }

    fun getTvSeason(seasonNumber: Int) {
        viewModelScope.launch {
            tvSeriesId.collectLatest { id ->
                id?.let {
                    _selectedSeasonNumber.emit(seasonNumber)

                    val season = tvSeriesRepository.getTvSeasons(
                        tvSeriesId = it,
                        seasonNumber = seasonNumber
                    )

                    val episodes = season.episodes
                    _episodes.emit(episodes)
                }
            }
        }
    }

}