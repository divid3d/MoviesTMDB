package com.example.moviesapp.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.person.PersonRepository
import com.example.moviesapp.ui.screens.destinations.PersonDetailsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val personRepository: PersonRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: PersonDetailsScreenArgs =
        PersonDetailsScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()

    private val personDetails: MutableStateFlow<PersonDetails?> = MutableStateFlow(null)

    private val combinedCredits: MutableStateFlow<CombinedCredits?> = MutableStateFlow(null)

    private val _externalIds: MutableStateFlow<ExternalIds?> = MutableStateFlow(null)
    private val externalIds: StateFlow<List<ExternalId>?> =
        _externalIds.filterNotNull().mapLatest { externalIds ->
            externalIds.toExternalIdList(type = ExternalContentType.Person)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val uiState: StateFlow<PersonDetailsScreenUiState> = combine(
        personDetails, combinedCredits, externalIds, error
    ) { details, combinedCredits, externalIds, error ->
        PersonDetailsScreenUiState(
            startRoute = navArgs.startRoute,
            details = details,
            externalIds = externalIds,
            credits = combinedCredits,
            error = error
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        PersonDetailsScreenUiState.getDefault(navArgs.startRoute)
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            deviceLanguage.collectLatest { deviceLanguage ->
                personRepository.getPersonDetails(
                    personId = navArgs.personId,
                    deviceLanguage = deviceLanguage
                ).request { response ->
                    response.onSuccess {
                        viewModelScope.launch {
                            personDetails.emit(data)
                        }
                    }

                    response.onFailure {
                        onFailure(this)
                    }

                    response.onException {
                        onError(this)
                    }
                }

                getPersonInfo(
                    personId = navArgs.personId,
                    deviceLanguage = deviceLanguage
                )
            }
        }
    }

    private fun getPersonInfo(personId: Int, deviceLanguage: DeviceLanguage) {
        getCombinedCredits(personId, deviceLanguage)
        getExternalIds(personId, deviceLanguage)
    }

    private fun getCombinedCredits(personId: Int, deviceLanguage: DeviceLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            personRepository.getCombinedCredits(
                personId = personId,
                deviceLanguage = deviceLanguage
            ).request { response ->
                response.onSuccess {
                    viewModelScope.launch {
                        combinedCredits.emit(data)
                    }
                }

                response.onFailure {
                    onFailure(this)
                }

                response.onException {
                    onError(this)
                }
            }
        }
    }

    private fun getExternalIds(personId: Int, deviceLanguage: DeviceLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            personRepository.getExternalIds(
                personId = personId,
                deviceLanguage = deviceLanguage
            ).request { response ->
                response.onSuccess {
                    viewModelScope.launch {
                        _externalIds.emit(data)
                    }
                }

                response.onFailure {
                    onFailure(this)
                }

                response.onException {
                    onError(this)
                }
            }
        }
    }
}