package com.example.moviesapp.ui.screens.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.DeviceRepository
import com.example.moviesapp.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val personRepository: PersonRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = deviceRepository.deviceLanguage
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
                                onError(message)
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
                    onError(message)
                }
            }
        }
    }
}