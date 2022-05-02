package com.example.moviesapp.use_case

import com.example.moviesapp.api.ApiResponse
import com.example.moviesapp.api.awaitApiResponse
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.PersonDetails
import com.example.moviesapp.repository.person.PersonRepository
import com.example.moviesapp.use_case.interfaces.GetPersonDetailsUseCase
import javax.inject.Inject

class GetPersonDetailsUseCaseImpl @Inject constructor(
    private val personRepository: PersonRepository
) : GetPersonDetailsUseCase {
    override suspend operator fun invoke(
        personId: Int,
        deviceLanguage: DeviceLanguage
    ): ApiResponse<PersonDetails> {
        return personRepository.getPersonDetails(
            personId = personId,
            deviceLanguage = deviceLanguage
        ).awaitApiResponse()
    }
}