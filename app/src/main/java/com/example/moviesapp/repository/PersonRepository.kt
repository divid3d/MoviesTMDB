package com.example.moviesapp.repository

import com.example.moviesapp.api.TmdbApiHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val apiHelper: TmdbApiHelper
) {
    fun getPersonDetails(personId: Int) = apiHelper.getPersonDetails(personId)

    fun getCombinedCredits(personId: Int) = apiHelper.getCombinedCredits(personId)
}