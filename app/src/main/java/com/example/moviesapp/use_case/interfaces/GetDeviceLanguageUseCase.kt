package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.DeviceLanguage
import kotlinx.coroutines.flow.Flow

interface GetDeviceLanguageUseCase {
    operator fun invoke(): Flow<DeviceLanguage>
}