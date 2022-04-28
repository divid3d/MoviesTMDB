package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.Review
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.repository.tv.TvSeriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMediaTypeReviewsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvSeriesRepository: TvSeriesRepository
) {
    operator fun invoke(
        mediaId: Int,
        type: MediaType,
    ): Flow<PagingData<Review>> {
        return when (type) {
            MediaType.Movie -> movieRepository.movieReviews(mediaId)
            MediaType.Tv -> tvSeriesRepository.tvSeriesReviews(mediaId)
            else -> emptyFlow()
        }
    }
}