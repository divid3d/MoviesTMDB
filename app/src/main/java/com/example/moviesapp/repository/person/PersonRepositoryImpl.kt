package com.example.moviesapp.repository.person

import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.model.DeviceLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
    private val apiHelper: TmdbApiHelper
) : PersonRepository {
    override fun getPersonDetails(personId: Int, deviceLanguage: DeviceLanguage) =
        apiHelper.getPersonDetails(personId, deviceLanguage.languageCode)

    override fun getCombinedCredits(personId: Int, deviceLanguage: DeviceLanguage) =
        apiHelper.getCombinedCredits(personId, deviceLanguage.languageCode)

    override fun getExternalIds(personId: Int, deviceLanguage: DeviceLanguage) =
        apiHelper.getPersonExternalIds(personId, deviceLanguage.languageCode)
}