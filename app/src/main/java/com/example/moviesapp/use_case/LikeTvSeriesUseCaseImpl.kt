package com.example.moviesapp.use_case

import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.LikeTvSeriesUseCase
import javax.inject.Inject

class LikeTvSeriesUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : LikeTvSeriesUseCase {
    override operator fun invoke(details: TvSeriesDetails) {
        return favouritesRepository.likeTvSeries(details)
    }
}