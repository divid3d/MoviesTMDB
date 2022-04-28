package com.example.moviesapp.use_case

import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.config.ConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDeviceLanguageUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    operator fun invoke(): Flow<DeviceLanguage> {
        return configRepository.getDeviceLanguage()
    }
}