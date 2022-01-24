package com.example.moviesapp.ui.screens.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.other.appendUrls
import com.example.moviesapp.repository.ConfigRepository
import com.example.moviesapp.repository.MovieRepository
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
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val queryDelay = 500.milliseconds
    private val config = configRepository.config

    private val _query: MutableStateFlow<String?> = MutableStateFlow(null)
    val query: StateFlow<String?> = _query.asStateFlow()

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.Init)
    val searchState: StateFlow<SearchState> = _searchState
        .asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), SearchState.Init)

    private val _queryLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val queryLoading: StateFlow<Boolean> = _queryLoading.asStateFlow()

    private var queryJob: Job? = null

    fun onQueryChange(query: String) {
        viewModelScope.launch {
            _query.emit(query)

            queryJob?.cancel()

            if (query.isBlank()) {
                _searchState.emit(SearchState.Init)
            } else {
                queryJob = createQueryJob(query).apply {
                    start()
                }
            }
        }
    }

    fun onQueryClear() {
        onQueryChange("")
    }

    private fun createQueryJob(query: String): Job {
        return viewModelScope.launch {
            try {
                delay(queryDelay)

                _queryLoading.emit(true)

                val response = movieRepository.movieSearch(query = query)
                    .combine(config) { moviePagingData, config ->
                        moviePagingData.map { movie ->
                            movie.appendUrls(config) as Presentable
                        }
                    }

                _searchState.emit(SearchState.Result(response))
            } catch (e: CancellationException) {

            } finally {
                withContext(NonCancellable) {
                    _queryLoading.emit(false)
                }
            }
        }
    }

}


sealed class SearchState {
    object Init : SearchState()
    data class Result(val data: Flow<PagingData<Presentable>>) : SearchState()
}