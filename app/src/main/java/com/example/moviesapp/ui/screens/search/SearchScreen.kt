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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
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
            modifier = Modifier.fillMaxWidth(),
            query = query,
            loading = queryLoading,
            onQueryChange = viewModel::onQueryChange,
            onQueryClear = viewModel::onQueryClear
        )
        Crossfade(
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
                        Text(text = "Brak wyników wyszukiwania")
                    }
                }
                is SearchState.Init -> {
                    Text(text = "Wpisz aby wyszukać")
                }
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
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = query.orEmpty(),
            onValueChange = onQueryChange,
            placeholder = {
                Text("Wpisz aby wyszukać")
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
                onSearch = { keyboardController?.hide() }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )
    }

}