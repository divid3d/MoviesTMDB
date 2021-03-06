package com.example.moviesapp.ui.screens.search

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.other.CaptureSpeechToText
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.sections.PresentableGridSection
import com.example.moviesapp.ui.components.sections.SearchGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.ScannerScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.search.components.QueryTextField
import com.example.moviesapp.ui.screens.search.components.SearchEmptyState
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun AnimatedVisibilityScope.SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<ScannerScreenDestination, String>
) {
    val uiState by viewModel.uiState.collectAsState()

    val onQueryChanged: (query: String) -> Unit = viewModel::onQueryChange
    val onQueryCleared: () -> Unit = viewModel::onQueryClear
    val onAddSearchQuerySuggestion: (SearchQuery) -> Unit = viewModel::addQuerySuggestion

    val onCameraClicked = {
        navigator.navigate(ScannerScreenDestination)
    }
    val onResultClicked: (id: Int, type: MediaType) -> Unit = { id, type ->
        val destination = when (type) {
            MediaType.Movie -> {
                MovieDetailsScreenDestination(
                    movieId = id,
                    startRoute = SearchScreenDestination.route
                )
            }

            MediaType.Tv -> {
                TvSeriesDetailsScreenDestination(
                    tvSeriesId = id,
                    startRoute = SearchScreenDestination.route
                )
            }

            else -> null
        }

        if (destination != null) {
            val searchQuery = SearchQuery(
                query = uiState.query.orEmpty(),
                lastUseDate = Date()
            )
            onAddSearchQuerySuggestion(searchQuery)

            navigator.navigate(destination)
        }
    }
    val onMovieClicked = { movieId: Int ->
        val destination = MovieDetailsScreenDestination(
            movieId = movieId,
            startRoute = SearchScreenDestination.route
        )

        navigator.navigate(destination)
    }
    val onQuerySuggestionSelected: (String) -> Unit = viewModel::onQuerySuggestionSelected

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Value -> {
                viewModel.onQueryChange(result.value)
            }
            else -> Unit
        }
    }

    SearchScreenContent(
        uiState = uiState,
        onQueryChanged = onQueryChanged,
        onQueryCleared = onQueryCleared,
        onResultClicked = onResultClicked,
        onCameraClicked = onCameraClicked,
        onMovieClicked = onMovieClicked,
        onQuerySuggestionSelected = onQuerySuggestionSelected
    )
}

@ExperimentalFoundationApi
@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    onQueryChanged: (query: String) -> Unit,
    onQueryCleared: () -> Unit,
    onResultClicked: (id: Int, type: MediaType) -> Unit,
    onCameraClicked: () -> Unit = {},
    onMovieClicked: (Int) -> Unit,
    onQuerySuggestionSelected: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val queryTextFieldFocusRequester = remember { FocusRequester() }
    val clearFocus = { focusManager.clearFocus(force = true) }

    val speechToTextLauncher = rememberLauncherForActivityResult(CaptureSpeechToText()) { result ->
        if (result != null) {
            focusManager.clearFocus()
            onQueryChanged(result)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .statusBarsPadding(),
    ) {
        QueryTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .animateContentSize(),
            query = uiState.query,
            suggestions = uiState.suggestions,
            voiceSearchAvailable = uiState.searchOptionsState.voiceSearchAvailable,
            cameraSearchAvailable = uiState.searchOptionsState.cameraSearchAvailable,
            loading = uiState.queryLoading,
            showClearButton = uiState.searchState !is SearchState.EmptyQuery,
            focusRequester = queryTextFieldFocusRequester,
            info = {
                AnimatedVisibility(
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    visible = uiState.searchState is SearchState.InsufficientQuery
                ) {
                    Text(
                        text = stringResource(R.string.search_insufficient_query_length_info_text),
                        fontSize = 12.sp
                    )
                }
            },
            onKeyboardSearchClicked = {
                clearFocus()
            },
            onQueryChange = onQueryChanged,
            onQueryClear = {
                onQueryCleared()
                queryTextFieldFocusRequester.requestFocus()
            },
            onVoiceSearchClick = {
                speechToTextLauncher.launch(null)
            },
            onCameraSearchClick = onCameraClicked,
            onSuggestionClick = { suggestion ->
                clearFocus()
                onQuerySuggestionSelected(suggestion)
            }
        )
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = uiState.resultState
        ) { state ->
            when (state) {
                is ResultState.Default -> {
                    val popular = state.popular.collectAsLazyPagingItems()

                    if (popular.isNotEmpty()) {
                        PresentableGridSection(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.small,
                                end = MaterialTheme.spacing.small,
                                bottom = MaterialTheme.spacing.large
                            ),
                            state = popular,
                            onPresentableClick = onMovieClicked
                        )
                    }
                }

                is ResultState.Search -> {
                    val result = state.result.collectAsLazyPagingItems()

                    if (result.isNotEmpty()) {
                        SearchGridSection(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.small,
                                end = MaterialTheme.spacing.small,
                                bottom = MaterialTheme.spacing.large
                            ),
                            state = result,
                            onSearchResultClick = { id, mediaType ->
                                clearFocus()
                                onResultClicked(id, mediaType)
                            }
                        )
                    } else {
                        SearchEmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.spacing.medium)
                                .padding(top = MaterialTheme.spacing.extraLarge),
                            onEditButtonClicked = {
                                queryTextFieldFocusRequester.requestFocus()
                            }
                        )
                    }
                }
            }
        }
    }
}

