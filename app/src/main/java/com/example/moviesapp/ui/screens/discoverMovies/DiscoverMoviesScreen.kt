package com.example.moviesapp.ui.screens.discoverMovies

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.SortOrder
import com.example.moviesapp.other.isEmpty
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.FilterEmptyState
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.discoverMovies.components.FilterFloatingButton
import com.example.moviesapp.ui.screens.discoverMovies.components.FilterModalBottomSheetContent
import com.example.moviesapp.ui.screens.discoverMovies.components.SortTypeDropdownButton
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class,
    FlowPreview::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Destination
@Composable
fun DiscoverMoviesScreen(
    viewModel: DiscoverMoviesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val sortType by viewModel.sortType.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val filterState by viewModel.filterState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val gridState = rememberLazyListState()

    val showFloatingButton = if (gridState.isScrollInProgress) {
        false
    } else {
        !sheetState.isVisible
    }

    val orderIconRotation by animateFloatAsState(targetValue = if (sortOrder == SortOrder.Desc) 0f else 180f)


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        scrimColor = Color.Black.copy(0.5f),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            FilterModalBottomSheetContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .navigationBarsPadding(),
                sheetState = sheetState,
                filterState = filterState,
                onCloseClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                },
                onSaveFilterClick = { filterState ->
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    viewModel.onFilterStateChange(filterState)
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AppBar(
                    title = stringResource(R.string.discover_movies_appbar_title),
                    action = {
                        IconButton(onClick = { navigator.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "go back",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }, trailing = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                modifier = Modifier.rotate(orderIconRotation),
                                onClick = {
                                    val order =
                                        if (sortOrder == SortOrder.Desc) SortOrder.Asc else SortOrder.Desc
                                    viewModel.onSortOrderChange(order)
                                }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_baseline_arrow_downward_24),
                                    contentDescription = "sort order",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                                )
                            }

                            SortTypeDropdownButton(
                                selectedType = sortType
                            ) { type ->
                                viewModel.onSortTypeChange(type)
                            }
                        }
                    })

                Crossfade(
                    modifier = Modifier.fillMaxSize(),
                    targetState = !movies.isEmpty()
                ) { hasFilterResults ->
                    if (hasFilterResults) {
                        PresentableGridSection(
                            modifier = Modifier.fillMaxSize(),
                            gridState = gridState,
                            contentPadding = PaddingValues(
                                horizontal = MaterialTheme.spacing.small,
                                vertical = MaterialTheme.spacing.medium,
                            ),
                            state = movies
                        ) { movieId ->
                            navigator.navigate(
                                MovieDetailsScreenDestination(movieId)
                            )
                        }
                    } else {
                        FilterEmptyState(
                            modifier = Modifier.fillMaxSize(),
                            onFilterButtonClicked = {
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomEnd),
                visible = showFloatingButton,
                enter = fadeIn(animationSpec = spring()) + scaleIn(
                    animationSpec = spring(),
                    initialScale = 0.3f
                ),
                exit = fadeOut(animationSpec = spring()) + scaleOut(
                    animationSpec = spring(),
                    targetScale = 0.3f
                )
            ) {
                FilterFloatingButton(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .navigationBarsWithImePadding(),
                    onClick = {
                        coroutineScope.launch {
                            if (sheetState.isVisible) {
                                sheetState.hide()
                            } else {
                                sheetState.show()
                            }
                        }
                    }
                )
            }
        }
    }
}

