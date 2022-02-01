package com.example.moviesapp.ui.screens.search

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.other.CaptureSpeechToText
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.SearchGridSection
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
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val query by viewModel.query.collectAsState()
    val queryLoading by viewModel.queryLoading.collectAsState()
    val searchState by viewModel.searchState.collectAsState()

    val focusManager = LocalFocusManager.current

    val speechToTextLauncher = rememberLauncherForActivityResult(CaptureSpeechToText()) { result ->
        if (result != null) {
            focusManager.clearFocus()
            viewModel.onQueryChange(result)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        QueryTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .animateContentSize(),
            query = query,
            loading = queryLoading,
            showClearButton = searchState !is SearchState.EmptyQuery,
            info = {
                AnimatedVisibility(
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    visible = searchState is SearchState.InsufficientQuery
                ) {
                    Text(
                        text = stringResource(R.string.search_insufficient_query_length_info_text),
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            },
            onQueryChange = viewModel::onQueryChange,
            onQueryClear = viewModel::onQueryClear,
            onVoiceSearchClick = {
                speechToTextLauncher.launch(null)
            }
        )
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = searchState
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
                            state = result
                        ) { resultId, mediaType ->
                            val destination = when (mediaType) {
                                MediaType.Movie -> MovieDetailsScreenDestination(
                                    movieId = resultId,
                                    startRoute = SearchScreenDestination.route
                                )

                                MediaType.Tv -> TvSeriesDetailsScreenDestination(
                                    tvSeriesId = resultId,
                                    startRoute = SearchScreenDestination.route
                                )

                                else -> null
                            }

                            if (destination != null) {
                                navigator.navigate(destination)
                            }
                        }
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

