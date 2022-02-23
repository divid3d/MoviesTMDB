package com.example.moviesapp.ui.screens.reviews

import androidx.paging.PagingData
import com.example.moviesapp.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ReviewsScreenUiState(
    val reviews: Flow<PagingData<Review>>
) {
    companion object {
        val default: ReviewsScreenUiState
            get() = ReviewsScreenUiState(reviews = emptyFlow())
    }
}