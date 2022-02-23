package com.example.moviesapp.ui.screens.browse

import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.TvSeriesType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.components.dialogs.InfoDialog
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class BrowseTvSeriesScreenArgs(
    val tvSeriesType: TvSeriesType
) : Parcelable

@OptIn(ExperimentalFoundationApi::class, kotlinx.coroutines.FlowPreview::class)
@Destination(navArgsDelegate = BrowseTvSeriesScreenArgs::class)
@Composable
fun BrowseTvSeriesScreen(
    viewModel: BrowseTvSeriesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onClearDialogConfirmClicked: () -> Unit = viewModel::onClearClicked
    val onTvSeriesClicked = { tvSeriesId: Int ->
        val destination = TvSeriesDetailsScreenDestination(
            tvSeriesId = tvSeriesId,
            startRoute = TvScreenDestination.route
        )

        navigator.navigate(destination)
    }

    BrowseTvSeriesScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onClearDialogConfirmClicked = onClearDialogConfirmClicked,
        onTvSeriesClicked = onTvSeriesClicked
    )
}

@Composable
fun BrowseTvSeriesScreenContent(
    uiState: BrowseTvSeriesScreenUiState,
    onBackClicked: () -> Unit,
    onClearDialogConfirmClicked: () -> Unit,
    onTvSeriesClicked: (tvSeriesId: Int) -> Unit
) {
    val tvSeries = uiState.tvSeries.collectAsLazyPagingItems()

    val appbarTitle = when (uiState.selectedTvSeriesType) {
        TvSeriesType.TopRated -> stringResource(R.string.all_tv_series_top_rated_label)
        TvSeriesType.AiringToday -> stringResource(R.string.all_tv_series_airing_today_label)
        TvSeriesType.Favourite -> stringResource(
            R.string.all_tv_series_favourites_label,
            uiState.favouriteMoviesCount
        )
        TvSeriesType.RecentlyBrowsed -> stringResource(R.string.all_tv_series_recently_browsed_label)
        TvSeriesType.Trending -> stringResource(R.string.all_tv_series_trending_label)
    }
    val showClearButton =
        uiState.selectedTvSeriesType == TvSeriesType.RecentlyBrowsed && tvSeries.itemSnapshotList.isNotEmpty()

    var showClearDialog by remember { mutableStateOf(false) }

    val showDialog = {
        showClearDialog = true
    }

    val dismissDialog = {
        showClearDialog = false
    }

    if (showClearDialog) {
        InfoDialog(
            infoText = stringResource(R.string.clear_recent_tv_series_dialog_text),
            onDismissRequest = dismissDialog,
            onCancelClick = dismissDialog,
            onConfirmClick = {
                onClearDialogConfirmClicked()
                dismissDialog()
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(title = appbarTitle, action = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "go back",
                    tint = MaterialTheme.colors.primary
                )
            }
        },
            trailing = {
                AnimatedVisibility(
                    visible = showClearButton,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
                        onClick = showDialog
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "clear recent",
                            tint = MaterialTheme.colors.primary
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
            state = tvSeries,
            onPresentableClick = onTvSeriesClicked
        )
    }
}