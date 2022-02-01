package com.example.moviesapp.ui.screens.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.Review
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val configRepository: ConfigRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val config = configRepository.config

    private val movieId: Flow<Int?> = savedStateHandle
        .getLiveData("movieId", null).asFlow()

    var review: Flow<PagingData<Review>>? = null

    init {
        viewModelScope.launch {
            movieId.collectLatest { movieId ->
                movieId?.let { id ->
                    review = movieRepository.movieReviews(id)
                        .cachedIn(viewModelScope)
                        .combine(config) { reviewsPagingData, config ->
                            reviewsPagingData.map { review ->
                                review.copy(
                                    authorDetails = review.authorDetails.appendUrls(config)
                                )
                            }
                        }
                }
            }
        }
    }

}