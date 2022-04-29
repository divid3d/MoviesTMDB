package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.MovieDetails

interface UnlikeMovieUseCase {
    operator fun invoke(details: MovieDetails)
}