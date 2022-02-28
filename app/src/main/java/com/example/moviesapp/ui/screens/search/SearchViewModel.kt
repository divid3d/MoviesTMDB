package com.example.moviesapp.ui.screens.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchResult
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val searchRepository: SearchRepository
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = configRepository.getDeviceLanguage()
    private val queryDelay = 500.milliseconds
    private val minQueryLength = 3

    private val voiceSearchAvailable: Flow<Boolean> = configRepository.getSpeechToTextAvailable()
    private val query: MutableStateFlow<String?> = MutableStateFlow(null)
    private val searchState: MutableStateFlow<SearchState> =
        MutableStateFlow(SearchState.EmptyQuery)
    private val queryLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var queryJob: Job? = null

    val uiState: StateFlow<SearchScreenUiState> = combine(
        voiceSearchAvailable, query, searchState, queryLoading
    ) { voiceSearchAvailable, query, searchState, queryLoading ->
        SearchScreenUiState(
            voiceSearchAvailable = voiceSearchAvailable,
            query = query,
            searchState = searchState,
            queryLoading = queryLoading
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SearchScreenUiState.default)

    fun onQueryChange(queryText: String) {
        viewModelScope.launch {
            query.emit(queryText)

            queryJob?.cancel()

            when {
                queryText.isBlank() -> {
                    searchState.emit(SearchState.EmptyQuery)
                }

                queryText.length < minQueryLength -> {
                    searchState.emit(SearchState.InsufficientQuery)
                }

                else -> {
                    queryJob = createQueryJob(queryText).apply {
                        start()
                    }
                }
            }
        }
    }

    fun onQueryClear() {
        onQueryChange("")
    }

    @OptIn(FlowPreview::class)
    private fun createQueryJob(query: String): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(queryDelay)

                queryLoading.emit(true)

                val response = deviceLanguage.map { deviceLanguage ->
                    searchRepository.multiSearch(
                        query = query,
                        deviceLanguage = deviceLanguage
                    )
                }.flattenMerge()

                searchState.emit(
                    SearchState.Result(
                        query = query,
                        data = response
                    )
                )
            } catch (_: CancellationException) {

            } finally {
                withContext(NonCancellable) {
                    queryLoading.emit(false)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        queryJob?.cancel()
    }
}


sealed class SearchState {
    object EmptyQuery : SearchState()
    object InsufficientQuery : SearchState()
    data class Result(
        val query: String,
        val data: Flow<PagingData<SearchResult>>
    ) : SearchState()
}