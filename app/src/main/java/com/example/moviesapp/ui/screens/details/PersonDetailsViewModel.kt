package com.example.moviesapp.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.PersonRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val configRepository: ConfigRepository,
    private val personRepository: PersonRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val personId: Flow<Int?> = savedStateHandle.getLiveData<Int>("personId").asFlow()

    private val _personDetails: MutableStateFlow<PersonDetails?> = MutableStateFlow(null)
    val personDetails: StateFlow<PersonDetails?> =
        _personDetails.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val _combinedCredits: MutableStateFlow<CombinedCredits?> = MutableStateFlow(null)

    val cast: StateFlow<List<CombinedCreditsCast>> =
        _combinedCredits
            .map { credits -> credits?.cast ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    val crew: StateFlow<List<CombinedCreditsCrew>> =
        _combinedCredits
            .map { credits -> credits?.crew ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    private val _externalIds: MutableStateFlow<ExternalIds?> = MutableStateFlow(null)
    val externalIds: StateFlow<List<ExternalId>?> =
        _externalIds.filterNotNull().map { externalIds ->
            externalIds.toExternalIdList(type = ExternalContentType.Person)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            personId.collectLatest { personId ->
                personId?.let { id ->
                    deviceLanguage.collectLatest { deviceLanguage ->
                        personRepository.getPersonDetails(
                            personId = id,
                            deviceLanguage = deviceLanguage
                        ).request { response ->
                            response.onSuccess {
                                viewModelScope.launch {
                                    _personDetails.emit(data)
                                }
                            }

                            response.onFailure {
                                onError(message)
                            }

                            response.onException {
                                onError()
                                firebaseCrashlytics.recordException(exception)
                            }
                        }

                        getPersonInfo(
                            personId = id,
                            deviceLanguage = deviceLanguage
                        )
                    }
                }
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
                        _combinedCredits.emit(data)
                    }
                }

                response.onFailure {
                    onError(message)
                }

                response.onException {
                    onError()
                    firebaseCrashlytics.recordException(exception)
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
                    onError(message)
                }

                response.onException {
                    onError()
                    firebaseCrashlytics.recordException(exception)
                }
            }
        }
    }

}