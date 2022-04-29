package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.use_case.interfaces.GetFavouritesMoviesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouritesMoviesUseCaseImpl @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
) : GetFavouritesMoviesUseCase {
    override operator fun invoke(): Flow<PagingData<MovieFavourite>> {
        return favouritesRepository.favouriteMovies()
    }
}