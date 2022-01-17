package com.example.moviesapp.ui.screens.tv

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.Config
import com.example.moviesapp.model.TvSeriesDetails
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

    private val _tvSeriesDetails: MutableStateFlow<TvSeriesDetails?> = MutableStateFlow(null)

    private val tvSeriesId: Flow<Int?> = savedStateHandle.getLiveData<Int>("tvSeriesId").asFlow()


    val tvSeriesDetails: StateFlow<TvSeriesDetails?> = combine(
        _tvSeriesDetails, config
    ) { movieDetails, config ->
        val posterUrl = config?.getImageUrl(movieDetails?.posterPath)
        val backdropUrl = config?.getImageUrl(movieDetails?.backdropPath)

        movieDetails?.copy(
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    init {
        viewModelScope.launch {
            tvSeriesId.collectLatest { tvSeriesId ->
                tvSeriesId?.let { id ->
                    getTvSeriesInfo(id)
                }
            }
        }
    }

    fun onLikeClick(tvSeriesDetails: TvSeriesDetails) {
        //favouritesRepository.likeMovie(movieDetails)
    }

    fun onUnlikeClick(tvSeriesDetails: TvSeriesDetails) {
        //favouritesRepository.unlikeMovie(movieDetails)
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

}