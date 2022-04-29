package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.Review
import kotlinx.coroutines.flow.Flow

interface GetMediaTypeReviewsUseCase {
    operator fun invoke(
        mediaId: Int,
        type: MediaType,
    ): Flow<PagingData<Review>>
}