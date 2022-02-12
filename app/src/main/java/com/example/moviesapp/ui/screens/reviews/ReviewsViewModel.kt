package com.example.moviesapp.ui.screens.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.Review
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.MovieRepository
import com.example.moviesapp.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: Flow<ReviewsScreenNavArgs?> = savedStateHandle
        .getLiveData("navArgs", null).asFlow()

    var review: Flow<PagingData<Review>>? = null

    init {
        viewModelScope.launch {
            navArgs.collectLatest { args ->
                args?.let { (id, type) ->
                    when (type) {
                        MediaType.Movie -> {
                            review = movieRepository.movieReviews(id)
                                .cachedIn(viewModelScope)
                        }
                        MediaType.Tv -> {
                            review = tvSeriesRepository.tvSeriesReviews(id)
                                .cachedIn(viewModelScope)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

}