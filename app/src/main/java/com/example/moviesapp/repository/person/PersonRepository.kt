package com.example.moviesapp.repository.person

import com.example.moviesapp.model.CombinedCredits
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.ExternalIds
import com.example.moviesapp.model.PersonDetails
import retrofit2.Call

interface PersonRepository {
    fun getPersonDetails(
        personId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<PersonDetails>

    fun getCombinedCredits(
        personId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<CombinedCredits>

    fun getExternalIds(
        personId: Int,
        deviceLanguage: DeviceLanguage = DeviceLanguage.default
    ): Call<ExternalIds>
}