package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetCameraAvailableUseCase
import javax.inject.Inject

class GetCameraAvailableUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
) : GetCameraAvailableUseCase {
    override operator fun invoke() = configRepository.getCameraAvailable()
}