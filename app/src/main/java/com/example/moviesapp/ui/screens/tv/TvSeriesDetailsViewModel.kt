package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.data.TvSeriesDetailsResponseDataSource
import com.example.moviesapp.model.*
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
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
            isFavourite = details.id in favouriteIds
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    init {
        viewModelScope.launch {
            tvSeriesId.collectLatest { tvSeriesId ->
                tvSeriesId?.let { id ->
                    val similarTvSeriesLoader: suspend (Int, Int, String) -> TvSeriesResponse =
                        tvSeriesRepository::similarTvSeries

                    val tvSeriesRecommendationLoader: suspend (Int, Int, String) -> TvSeriesResponse =
                        tvSeriesRepository::tvSeriesRecommendations

                    similarTvSeries = Pager(PagingConfig(pageSize = 20)) {
                        TvSeriesDetailsResponseDataSource(
                            tvSeriesId = id,
                            apiHelperMethod = similarTvSeriesLoader
                        )
                    }.flow.combine(config) { moviePagingData, config ->
                        moviePagingData.map { tvSeries ->
                            tvSeries.appendUrls(config)
                        }
                    }

                    tvSeriesRecommendations = Pager(PagingConfig(pageSize = 20)) {
                        TvSeriesDetailsResponseDataSource(
                            tvSeriesId = id,
                            apiHelperMethod = tvSeriesRecommendationLoader
                        )
                    }.flow.combine(config) { moviePagingData, config ->
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

    private fun TvSeries.appendUrls(
        config: Config?
    ): TvSeries {
        val tvSeriesPosterUrl = config?.getImageUrl(posterPath)
        val tvSeriesBackdropUrl = config?.getImageUrl(backdropPath, size = "w300")

        return copy(
            posterUrl = tvSeriesPosterUrl,
            backdropUrl = tvSeriesBackdropUrl,

            )
    }

    private fun Creator.appendUrls(
        config: Config?
    ): Creator {
        val profileUrl = config?.getImageUrl(profilePath, size = "w185")

        return copy(
            profileUrl = profileUrl
        )
    }

    private fun Episode.appendUrls(
        config: Config?
    ): Episode {
        val stillUrl = config?.getImageUrl(stillPath, size = "w300")

        return copy(
            stillUrl = stillUrl
        )
    }

    private fun Season.appendUrl(
        config: Config?
    ): Season {
        val posterUrl = config?.getImageUrl(posterPath)

        return copy(
            posterUrl = posterUrl
        )
    }

}