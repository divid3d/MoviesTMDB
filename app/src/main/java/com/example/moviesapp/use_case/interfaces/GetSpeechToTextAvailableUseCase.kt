package com.example.moviesapp.use_case.interfaces

import kotlinx.coroutines.flow.Flow

interface GetSpeechToTextAvailableUseCase {
    operator fun invoke(): Flow<Boolean>
}