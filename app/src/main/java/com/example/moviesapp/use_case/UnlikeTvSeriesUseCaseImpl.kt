package com.example.moviesapp.use_case

import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.UnlikeTvSeriesUseCase
import javax.inject.Inject

class UnlikeTvSeriesUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : UnlikeTvSeriesUseCase {
    override operator fun invoke(details: TvSeriesDetails) {
        return favouritesRepository.unlikeTvSeries(details)
    }
}