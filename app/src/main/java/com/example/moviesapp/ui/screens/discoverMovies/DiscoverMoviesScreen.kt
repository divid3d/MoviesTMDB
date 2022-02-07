package com.example.moviesapp.ui.screens.discoverMovies

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.SortOrder
import com.example.moviesapp.model.SortType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class, kotlinx.coroutines.FlowPreview::class)
@Destination
@Composable
fun DiscoverMoviesScreen(
    viewModel: DiscoverMoviesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val sortType by viewModel.sortType.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    var showSortTypeDropdown by remember { mutableStateOf(false) }

    val orderIconRotation by animateFloatAsState(targetValue = if (sortOrder == SortOrder.Desc) 0f else 180f)

    SortTypeDropDown(
        expanded = showSortTypeDropdown,
        onDismissRequest = {
            showSortTypeDropdown = false
        },
        selectedSortType = sortType,
        onSortTypeSelected = { type ->
            showSortTypeDropdown = false

            if (type != sortType) {
                viewModel.onSortTypeChange(type)
            }
        }
    )

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
                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(R.drawable.ic_baseline_filter_list_24),
                            contentDescription = "filter",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                        )
                    }
                    IconButton(
                        onClick = {
                            showSortTypeDropdown = true
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_baseline_sort_24),
                            contentDescription = "sort type",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                        )
                    }
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
                }
            })

        PresentableGridSection(
            modifier = Modifier.fillMaxSize(),
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
    }
}


@Composable
fun SortTypeDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit = {},
    selectedSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit = {}
) {
    val items = SortType.values().map { type -> type to type.getLabel() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            items.forEach { (type, labelResId) ->
                DropdownMenuItem(
                    onClick = { onSortTypeSelected(type) })
                {
                    Text(
                        text = stringResource(labelResId),
                        color = if (type == selectedSortType) MaterialTheme.colors.primary else Color.White
                    )
                }
            }
        }

    }
}