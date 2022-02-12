package com.example.moviesapp.ui.screens.related

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.MovieRelationInfo
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelatedMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var movies: Flow<PagingData<Presentable>>? = null

    private val movieRelationType: Flow<MovieRelationInfo?> = savedStateHandle
        .getLiveData("movieRelationInfo", null).asFlow()

    init {
        viewModelScope.launch {
            movieRelationType.collectLatest { relationType ->
                when (relationType?.type) {
                    RelationType.Similar -> {
                        val id = relationType.movieId

                        movies = movieRepository.similarMovies(id)
                            .cachedIn(viewModelScope)
                            .map { data -> data.map { movie -> movie } }
                    }

                    RelationType.Recommended -> {
                        val id = relationType.movieId

                        movies = movieRepository.moviesRecommendations(id)
                            .cachedIn(viewModelScope)
                            .map { data -> data.map { movie -> movie } }
                    }
                    else -> Unit
                }
            }
        }
    }
}