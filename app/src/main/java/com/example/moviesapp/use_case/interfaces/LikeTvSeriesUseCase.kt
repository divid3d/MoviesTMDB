package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.TvSeriesDetails

interface LikeTvSeriesUseCase {
    operator fun invoke(details: TvSeriesDetails)
}