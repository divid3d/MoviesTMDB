package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.repository.favourites.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavouritesMoviesUseCase @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
) {
    operator fun invoke(): Flow<PagingData<MovieFavourite>> {
        return favouritesRepository.favouriteMovies()
    }
}