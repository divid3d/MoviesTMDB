package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.ProviderSource
import kotlinx.coroutines.flow.Flow

interface GetAllMoviesWatchProvidersUseCase {
    operator fun invoke(): Flow<List<ProviderSource>>
}