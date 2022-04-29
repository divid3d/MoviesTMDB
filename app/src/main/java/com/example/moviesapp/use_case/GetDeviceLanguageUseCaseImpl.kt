package com.example.moviesapp.use_case

import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.use_case.interfaces.GetDeviceLanguageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDeviceLanguageUseCaseImpl @Inject constructor(
    private val configRepository: ConfigRepository
) : GetDeviceLanguageUseCase {
    override operator fun invoke(): Flow<DeviceLanguage> {
        return configRepository.getDeviceLanguage()
    }
}