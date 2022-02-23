package com.example.moviesapp.ui.screens.search

data class SearchScreenUiState(
    val voiceSearchAvailable: Boolean,
    val query: String?,
    val searchState: SearchState,
    val queryLoading: Boolean
) {
    companion object {
        val default: SearchScreenUiState
            get() = SearchScreenUiState(
                voiceSearchAvailable = false,
                query = null,
                searchState = SearchState.EmptyQuery,
                queryLoading = false
            )
    }
}