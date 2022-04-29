package com.example.moviesapp.ui.screens.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.model.SearchResult
import com.example.moviesapp.use_case.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getDeviceLanguageUseCaseImpl: GetDeviceLanguageUseCase,
    private val getSpeechToTextAvailableUseCase: GetSpeechToTextAvailableUseCase,
    private val mediaSearchQueriesUseCase: MediaSearchQueriesUseCase,
    private val mediaAddSearchQueryUseCase: MediaAddSearchQueryUseCase,
    private val getMediaMultiSearchUseCase: GetMediaMultiSearchUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : BaseViewModel() {

    private val deviceLanguage: Flow<DeviceLanguage> = getDeviceLanguageUseCaseImpl()
    private val queryDelay = 500.milliseconds
    private val minQueryLength = 3

    private val popularMovies: Flow<PagingData<Movie>> =
        deviceLanguage.mapLatest { deviceLanguage ->
            getPopularMoviesUseCase(deviceLanguage)
        }.flattenMerge().cachedIn(viewModelScope)

    private val voiceSearchAvailable: Flow<Boolean> = getSpeechToTextAvailableUseCase()
    private val queryState: MutableStateFlow<QueryState> = MutableStateFlow(QueryState.default)
    private val suggestions: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val searchState: MutableStateFlow<SearchState> =
        MutableStateFlow(SearchState.EmptyQuery)
    private val resultState: MutableStateFlow<ResultState> =
        MutableStateFlow(ResultState.Default(popularMovies))
    private val queryLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var queryJob: Job? = null

    val uiState: StateFlow<SearchScreenUiState> = combine(
        voiceSearchAvailable, queryState, suggestions, searchState, resultState
    ) { voiceSearchAvailable, queryState, suggestions, searchState, resultState ->
        SearchScreenUiState(
            voiceSearchAvailable = voiceSearchAvailable,
            query = queryState.query,
            suggestions = suggestions,
            searchState = searchState,
            resultState = resultState,
            queryLoading = queryState.loading
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SearchScreenUiState.default)

    fun onQueryChange(queryText: String) {
        viewModelScope.launch {
            queryState.emit(queryState.value.copy(query = queryText))

            queryJob?.cancel()

            when {
                queryText.isBlank() -> {
                    searchState.emit(SearchState.EmptyQuery)
                    suggestions.emit(emptyList())
                    resultState.emit(ResultState.Default(popularMovies))
                }

                queryText.length < minQueryLength -> {
                    searchState.emit(SearchState.InsufficientQuery)
                    suggestions.emit(emptyList())
                }

                else -> {
                    val querySuggestions = mediaSearchQueriesUseCase(queryText)
                    suggestions.emit(querySuggestions)

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

    fun onQuerySuggestionSelected(searchQuery: String) {
        if (queryState.value.query != searchQuery) {
            onQueryChange(searchQuery)
        }
    }

    fun addQuerySuggestion(searchQuery: SearchQuery) {
        mediaAddSearchQueryUseCase(searchQuery)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun createQueryJob(query: String): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(queryDelay)

                queryLoading.emit(true)

                val searchResults = deviceLanguage.mapLatest { deviceLanguage ->
                    getMediaMultiSearchUseCase(
                        query = query,
                        deviceLanguage = deviceLanguage
                    )
                }.flattenMerge().cachedIn(viewModelScope)

                searchState.emit(SearchState.ValidQuery)
                resultState.emit(ResultState.Search(searchResults))
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
    object ValidQuery : SearchState()
}

sealed class ResultState {
    data class Default(val popular: Flow<PagingData<Movie>> = emptyFlow()) : ResultState()
    data class Search(val result: Flow<PagingData<SearchResult>>) : ResultState()
}

data class QueryState(
    val query: String?,
    val loading: Boolean
) {
    companion object {
        val default: QueryState = QueryState(
            query = null,
            loading = false
        )
    }
}