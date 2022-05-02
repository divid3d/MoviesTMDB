package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.ExternalIds

interface GetPersonExternalIdsUseCase {
    suspend operator fun invoke(
        personId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<ExternalIds>
}