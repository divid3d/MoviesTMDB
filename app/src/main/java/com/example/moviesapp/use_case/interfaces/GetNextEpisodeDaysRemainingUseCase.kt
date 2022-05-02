package com.example.moviesapp.use_case.interfaces

import java.util.*

interface GetNextEpisodeDaysRemainingUseCase {
    operator fun invoke(nextEpisodeAirDate: Date): Long
}