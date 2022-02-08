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
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.*
import com.example.moviesapp.other.isEmpty
import com.example.moviesapp.other.singleDecimalPlaceFormatted
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, FlowPreview::class, ExperimentalMaterialApi::class,
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

    var showSortTypeDropdown by remember { mutableStateOf(false) }

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

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        scrimColor = Color.Black.copy(0.5f),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            FilterModalBottomSheetContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(MaterialTheme.spacing.medium),
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

@Composable
fun FilterFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        onClick = onClick
    ) {
        Image(
            painter = painterResource(R.drawable.ic_baseline_filter_list_24),
            contentDescription = "filter"
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterModalBottomSheetContent(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    filterState: FilterState,
    onCloseClick: () -> Unit = {},
    onSaveFilterClick: (FilterState) -> Unit = {}
) {
    var currentFilterState by remember(filterState, sheetState.isVisible) {
        mutableStateOf(filterState)
    }

    val enableSaveButton = currentFilterState != filterState

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier.width(64.dp),
            color = Color.White.copy(0.3f),
            thickness = 4.dp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close filter",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            FilterGenresSection(
                modifier = Modifier.fillMaxWidth(),
                genres = currentFilterState.selectedGenres,
                onGenreClick = { genre ->
                    currentFilterState = currentFilterState.copy(
                        selectedGenres = currentFilterState.selectedGenres.minus(genre)
                    )
                }
            )
            SectionDivider(modifier = Modifier.fillMaxWidth())
            FilterGenresSection(
                modifier = Modifier.fillMaxWidth(),
                genres = currentFilterState.run {
                    availableGenres.filterNot { genre ->
                        genre.id in selectedGenres.map { selectedGenre -> selectedGenre.id }
                    }
                },
                onGenreClick = { genre ->
                    currentFilterState = currentFilterState.copy(
                        selectedGenres = currentFilterState.selectedGenres.plus(genre)
                    )
                }
            )
        }
        VoteRangeSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.medium),
            voteRange = currentFilterState.voteRange,
            onCurrentVoteRangeChange = { voteRange ->
                currentFilterState = currentFilterState.copy(
                    voteRange = currentFilterState.voteRange.copy(
                        current = voteRange
                    )
                )
            }
        )

        LabeledSwitch(
            modifier = Modifier.fillMaxWidth(),
            checked = currentFilterState.showOnlyWithPoster,
            onCheckedChanged = { show ->
                currentFilterState = currentFilterState.copy(
                    showOnlyWithPoster = show
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    currentFilterState = currentFilterState.clear()
                }) {
                Text(text = "Wyczyść")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = enableSaveButton,
                onClick = { onSaveFilterClick(currentFilterState) }) {
                Text(text = "Zapisz")
            }
        }
    }
}

@Composable
fun FilterGenresSection(
    modifier: Modifier = Modifier,
    genres: List<Genre>,
    onGenreClick: (Genre) -> Unit = {},
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = MaterialTheme.spacing.extraSmall,
        crossAxisSpacing = MaterialTheme.spacing.extraSmall
    ) {
        genres.map { genre ->
            GenreChip(
                text = genre.name,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VoteRangeSlider(
    modifier: Modifier = Modifier,
    voteRange: VoteRange,
    onCurrentVoteRangeChange: (ClosedFloatingPointRange<Float>) -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = voteRange.current.start.singleDecimalPlaceFormatted(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = voteRange.current.endInclusive.singleDecimalPlaceFormatted(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        RangeSlider(
            modifier = Modifier.fillMaxWidth(),
            values = voteRange.current,
            valueRange = voteRange.default,
            onValueChange = onCurrentVoteRangeChange
        )
    }
}

@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = MaterialTheme.spacing.medium),
            text = "Tylko z plakatami"
        )
        Switch(checked = checked, onCheckedChange = onCheckedChanged)
    }
}
