package com.example.moviesapp.ui.screens.search

import androidx.compose.runtime.Stable

@Stable
data class SearchScreenUiState(
    val voiceSearchAvailable: Boolean,
    val query: String?,
    val suggestions: List<String>,
    val searchState: SearchState,
    val resultState: ResultState,
    val queryLoading: Boolean
) {
    companion object {
        val default: SearchScreenUiState
            get() = SearchScreenUiState(
                voiceSearchAvailable = false,
                query = null,
                suggestions = emptyList(),
                searchState = SearchState.EmptyQuery,
                resultState = ResultState.Default(),
                queryLoading = false
            )
    }
}