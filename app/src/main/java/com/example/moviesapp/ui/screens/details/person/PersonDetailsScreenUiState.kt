package com.example.moviesapp.ui.screens.details.person

import androidx.compose.runtime.Stable
import com.example.moviesapp.model.CombinedCredits
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.model.PersonDetails

@Stable
data class PersonDetailsScreenUiState(
    val startRoute: String,
    val details: PersonDetails?,
    val externalIds: List<ExternalId>?,
    val credits: CombinedCredits?,
    val error: String?
) {
    companion object {
        fun getDefault(startRoute: String): PersonDetailsScreenUiState {
            return PersonDetailsScreenUiState(
                startRoute = startRoute,
                details = null,
                externalIds = null,
                credits = null,
                error = null
            )
        }
    }
}

@Stable
sealed class PersonDetailsState {
    object Loading : PersonDetailsState()
    object Error : PersonDetailsState()
    data class Result(val details: PersonDetails) : PersonDetailsState()
}