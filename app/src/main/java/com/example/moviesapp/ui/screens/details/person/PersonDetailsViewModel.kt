package com.example.moviesapp.ui.screens.details.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.model.*
import com.example.moviesapp.ui.screens.destinations.PersonDetailsScreenDestination
import com.example.moviesapp.use_case.interfaces.GetCombinedCreditsUseCase
import com.example.moviesapp.use_case.interfaces.GetDeviceLanguageUseCase
import com.example.moviesapp.use_case.interfaces.GetPersonDetailsUseCase
import com.example.moviesapp.use_case.interfaces.GetPersonExternalIdsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase,
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    private val getCombinedCreditsUseCase: GetCombinedCreditsUseCase,
    private val getPersonExternalIdsUseCase: GetPersonExternalIdsUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs: PersonDetailsScreenArgs =
        PersonDetailsScreenDestination.argsFrom(savedStateHandle)
    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCase()

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
        getPersonInfo()
    }

    private fun getPersonInfo() {
        viewModelScope.launch(defaultDispatcher) {
            supervisorScope {
                deviceLanguage.collectLatest { deviceLanguage ->
                    launch {
                        getPersonDetails(
                            personId = navArgs.personId,
                            deviceLanguage = deviceLanguage
                        )
                    }

                    launch {
                        getCombinedCredits(
                            personId = navArgs.personId,
                            deviceLanguage = deviceLanguage
                        )
                    }

                    launch {
                        getExternalIds(
                            personId = navArgs.personId,
                            deviceLanguage = deviceLanguage
                        )
                    }
                }
            }
        }
    }

    private suspend fun getPersonDetails(personId: Int, deviceLanguage: DeviceLanguage) {
        getPersonDetailsUseCase(
            personId = personId, deviceLanguage = deviceLanguage
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                personDetails.emit(data)
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }


    private suspend fun getCombinedCredits(personId: Int, deviceLanguage: DeviceLanguage) {
        getCombinedCreditsUseCase(
            personId = personId,
            deviceLanguage = deviceLanguage
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                combinedCredits.emit(data)
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }

    private suspend fun getExternalIds(personId: Int, deviceLanguage: DeviceLanguage) {
        getPersonExternalIdsUseCase(
            personId = personId,
            deviceLanguage = deviceLanguage
        ).onSuccess {
            viewModelScope.launch(defaultDispatcher) {
                _externalIds.emit(data)
            }
        }.onFailure {
            onFailure(this)
        }.onException {
            onError(this)
        }
    }
}