package com.example.moviesapp.repository

import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.DeviceLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val apiHelper: TmdbApiHelper
) {
    fun getPersonDetails(personId: Int, deviceLanguage: DeviceLanguage = DeviceLanguage.default) =
        apiHelper.getPersonDetails(personId, deviceLanguage.languageCode)

    fun getCombinedCredits(personId: Int, deviceLanguage: DeviceLanguage = DeviceLanguage.default) =
        apiHelper.getCombinedCredits(personId, deviceLanguage.languageCode)
}