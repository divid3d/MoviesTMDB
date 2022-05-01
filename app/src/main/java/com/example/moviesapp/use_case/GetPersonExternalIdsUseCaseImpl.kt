package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.ExternalIds
import com.example.moviesapp.repository.person.PersonRepository
import com.example.moviesapp.use_case.interfaces.GetPersonExternalIdsUseCase
import javax.inject.Inject

class GetPersonExternalIdsUseCaseImpl @Inject constructor(
    private val personRepository: PersonRepository
) : GetPersonExternalIdsUseCase {
    override suspend operator fun invoke(
        personId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<ExternalIds> {
        return personRepository.getExternalIds(
            personId = personId,
            deviceLanguage = deviceLanguage
        ).awaitApiResponse()
    }
}