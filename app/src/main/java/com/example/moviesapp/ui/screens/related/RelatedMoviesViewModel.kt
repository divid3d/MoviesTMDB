package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieRelationInfo
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.DeviceRepository
import com.example.moviesapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RelatedMoviesViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = deviceRepository.deviceLanguage
    var movies: Flow<PagingData<Movie>>? = null

    private val movieRelationType: Flow<MovieRelationInfo?> = savedStateHandle
        .getLiveData("movieRelationInfo", null).asFlow()

    init {
        viewModelScope.launch {
            movieRelationType.collectLatest { relationType ->
                when (relationType?.type) {
                    RelationType.Similar -> {
                        val id = relationType.movieId

                        movies = deviceLanguage.map { deviceLanguage ->
                            movieRepository.similarMovies(
                                movieId = id,
                                deviceLanguage = deviceLanguage
                            )
                        }.flattenMerge().cachedIn(viewModelScope)
                    }

                    RelationType.Recommended -> {
                        val id = relationType.movieId

                        movies = deviceLanguage.map { deviceLanguage ->
                            movieRepository.moviesRecommendations(
                                movieId = id,
                                deviceLanguage = deviceLanguage
                            )
                        }.flattenMerge().cachedIn(viewModelScope)
                    }
                    else -> Unit
                }
            }
        }
    }
}