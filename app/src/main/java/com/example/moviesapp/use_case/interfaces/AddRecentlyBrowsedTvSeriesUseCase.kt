package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.TvSeriesDetails

interface AddRecentlyBrowsedTvSeriesUseCase {
    operator fun invoke(details: TvSeriesDetails)
}