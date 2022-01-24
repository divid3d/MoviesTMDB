package com.example.moviesapp.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.theme.White300
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.systemBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: SearchViewModel = hiltViewModel()

    val query by viewModel.query.collectAsState()
    val queryLoading by viewModel.queryLoading.collectAsState()
    val searchState by viewModel.searchState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        QueryTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            query = query,
            loading = queryLoading,
            onQueryChange = viewModel::onQueryChange,
            onQueryClear = viewModel::onQueryClear
        )
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = searchState
        ) { state ->
            when (state) {
                is SearchState.Result -> {
                    val result = state.data.collectAsLazyPagingItems()

                    val empty by derivedStateOf {
                        result.run {
                            loadState.append.endOfPaginationReached && itemSnapshotList.isEmpty()
                        }
                    }

                    if (!empty) {
                        PresentableGridSection(
                            modifier = Modifier.fillMaxSize(),
                            showRefreshItems = false,
                            state = result
                        ) { movieId ->
                            navigator.navigate(
                                MovieDetailsScreenDestination(
                                    movieId = movieId,
                                    startRoute = SearchScreenDestination.route
                                )
                            )
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QueryTextField(
    modifier: Modifier = Modifier,
    query: String?,
    loading: Boolean = false,
    onQueryChange: (String) -> Unit = {},
    onQueryClear: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = query.orEmpty(),
            onValueChange = onQueryChange,
            placeholder = {
                Text(text = stringResource(R.string.search_placeholder))
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        visible = loading
                    ) {
                        CircularProgressIndicator()
                    }
                    IconButton(onClick = onQueryClear) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "clear"
                        )
                    }
                }
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus(force = true)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            singleLine = true
        )
    }

}

@Composable
fun SearchEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            imageVector = Icons.Filled.Info,
            tint = White300,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = "Brak wyników wyszukiwania",
            style = TextStyle(color = White300)
        )
    }
}