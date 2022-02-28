package com.example.moviesapp.repository.favourites

import androidx.paging.PagingData
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.model.MovieFavourite
import com.example.moviesapp.model.TvSeriesDetails
import com.example.moviesapp.model.TvSeriesFavourite
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun likeMovie(movieDetails: MovieDetails)

    fun likeTvSeries(tvSeriesDetails: TvSeriesDetails)

    fun unlikeMovie(movieDetails: MovieDetails)

    fun unlikeTvSeries(tvSeriesDetails: TvSeriesDetails)

    fun favouriteMovies(): Flow<PagingData<MovieFavourite>>

    fun favouritesTvSeries(): Flow<PagingData<TvSeriesFavourite>>

    fun getFavouritesMoviesIds(): Flow<List<Int>>

    fun getFavouriteTvSeriesIds(): Flow<List<Int>>

    fun getFavouriteMoviesCount(): Flow<Int>

    fun getFavouriteTvSeriesCount(): Flow<Int>
}