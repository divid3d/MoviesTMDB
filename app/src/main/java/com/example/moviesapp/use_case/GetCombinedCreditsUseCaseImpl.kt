package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.CombinedCredits
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.repository.person.PersonRepository
import com.example.moviesapp.use_case.interfaces.GetCombinedCreditsUseCase
import javax.inject.Inject

class GetCombinedCreditsUseCaseImpl @Inject constructor(
    private val personRepository: PersonRepository
) : GetCombinedCreditsUseCase {
    override suspend operator fun invoke(
        personId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<CombinedCredits> {
        return personRepository.getCombinedCredits(
            personId = personId,
            deviceLanguage = deviceLanguage
        ).awaitApiResponse()
    }
}