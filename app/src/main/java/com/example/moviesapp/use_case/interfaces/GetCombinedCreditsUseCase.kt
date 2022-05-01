package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.model.CombinedCredits
import com.example.moviesapp.model.DeviceLanguage

interface GetCombinedCreditsUseCase {
    suspend operator fun invoke(
        personId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<CombinedCredits>
}