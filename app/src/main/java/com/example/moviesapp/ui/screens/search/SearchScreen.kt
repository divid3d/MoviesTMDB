package com.example.moviesapp.ui.screens.search

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.other.CaptureSpeechToText
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.sections.SearchGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.search.components.QueryTextField
import com.example.moviesapp.ui.screens.search.components.SearchEmptyState
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AnimatedVisibilityScope.SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onQueryChanged: (query: String) -> Unit = viewModel::onQueryChange
    val onQueryCleared: () -> Unit = viewModel::onQueryClear
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
            navigator.navigate(destination)
        }
    }

    SearchScreenContent(
        uiState = uiState,
        onQueryChanged = onQueryChanged,
        onQueryCleared = onQueryCleared,
        onResultClicked = onResultClicked
    )
}

@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    onQueryChanged: (query: String) -> Unit,
    onQueryCleared: () -> Unit,
    onResultClicked: (id: Int, type: MediaType) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
            voiceSearchAvailable = uiState.voiceSearchAvailable,
            loading = uiState.queryLoading,
            showClearButton = uiState.searchState !is SearchState.EmptyQuery,
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
            onQueryChange = onQueryChanged,
            onQueryClear = onQueryCleared,
            onVoiceSearchClick = {
                speechToTextLauncher.launch(null)
            }
        )
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = uiState.searchState
        ) { state ->
            when (state) {
                is SearchState.Result -> {
                    val result = state.data.collectAsLazyPagingItems()

                    if (result.isNotEmpty()) {
                        SearchGridSection(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = MaterialTheme.spacing.small,
                                vertical = MaterialTheme.spacing.medium,
                            ),
                            state = result,
                            onSearchResultClick = onResultClicked
                        )
                    } else {
                        SearchEmptyState(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}

