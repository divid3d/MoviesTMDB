package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.MovieDetails

interface LikeMovieUseCase {
    operator fun invoke(details: MovieDetails)
}