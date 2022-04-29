package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetSpeechToTextAvailableUseCase
import javax.inject.Inject

class GetSpeechToTextAvailableUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
) : GetSpeechToTextAvailableUseCase {
    override operator fun invoke() = configRepository.getSpeechToTextAvailable()
}