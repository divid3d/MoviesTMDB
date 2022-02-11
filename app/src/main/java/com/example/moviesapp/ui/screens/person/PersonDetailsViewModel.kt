package com.example.moviesapp.ui.screens.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.api.onException
import com.example.moviesapp.api.onFailure
import com.example.moviesapp.api.onSuccess
import com.example.moviesapp.api.request
import com.example.moviesapp.model.*
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.other.asFlow
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val personRepository: PersonRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val config: StateFlow<Config?> = configRepository.config
    private val personId: Flow<Int?> = savedStateHandle.getLiveData<Int>("personId").asFlow()

    private val _personDetails: MutableStateFlow<PersonDetails?> = MutableStateFlow(null)
    val personDetails: StateFlow<PersonDetails?> = combine(
        _personDetails, config
    ) { details, config ->
        details?.appendUrls(config)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    private val _combinedCredits: MutableStateFlow<CombinedCredits?> = MutableStateFlow(null)

    val cast: StateFlow<List<CombinedCreditsCast>> = combine(
        _combinedCredits, config
    ) { credits, config ->
        credits?.cast?.map { cast -> cast.appendUrls(config) } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    val crew: StateFlow<List<CombinedCreditsCrew>> = combine(
        _combinedCredits, config
    ) { credits, config ->
        credits?.crew?.map { crew -> crew.appendUrls(config) } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            personId.collectLatest { personId ->
                personId?.let { id ->
                    personRepository.getPersonDetails(id).request { response ->
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

                    getPersonInfo(id)
                }
            }
        }
    }

    private fun getPersonInfo(personId: Int) {
        getCombinedCredits(personId)
    }

    private fun getCombinedCredits(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            personRepository.getCombinedCredits(personId).request { response ->
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