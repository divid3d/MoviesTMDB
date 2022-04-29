package com.example.moviesapp.ui.screens.related

import android.os.Parcelable
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.components.sections.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelatedTvSeriesScreenArgs(
    val tvSeriesId: Int,
    val type: RelationType,
    val startRoute: String
) : Parcelable

@Destination(navArgsDelegate = RelatedTvSeriesScreenArgs::class)
@Composable
fun AnimatedVisibilityScope.RelatedTvSeriesScreen(
    viewModel: RelatedTvSeriesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackButtonClicked: () -> Unit = { navigator.navigateUp() }
    val onCloseClicked: () -> Unit = {
        navigator.popBackStack(uiState.startRoute, inclusive = false)
    }
    val onTvSeriesClicked: (tvSeriesId: Int) -> Unit = { id ->
        val destination = TvSeriesDetailsScreenDestination(
            tvSeriesId = id,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }

    RelatedTvSeriesScreenContent(
        uiState = uiState,
        onBackButtonClicked = onBackButtonClicked,
        onCloseClicked = onCloseClicked,
        onTvSeriesClicked = onTvSeriesClicked
    )
}

@Composable
fun RelatedTvSeriesScreenContent(
    uiState: RelatedTvSeriesScreenUiState,
    onBackButtonClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onTvSeriesClicked: (tvSeriesId: Int) -> Unit
) {
    val tvSeries = uiState.tvSeries.collectAsLazyPagingItems()

    val appbarTitle = stringResource(
        when (uiState.relationType) {
            RelationType.Similar -> R.string.related_tv_series_screen_similar_appbar_label
            RelationType.Recommended -> R.string.related_tv_series_screen_recommendations_appbar_label
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        AppBar(
            title = appbarTitle,
            action = {
                IconButton(onClick = onBackButtonClicked) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "go back",
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            trailing = {
                Row(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                    IconButton(onClick = onCloseClicked) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "close",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )

        PresentableGridSection(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                top = MaterialTheme.spacing.medium,
                start = MaterialTheme.spacing.small,
                end = MaterialTheme.spacing.small,
                bottom = MaterialTheme.spacing.large
            ),
            state = tvSeries,
            onPresentableClick = onTvSeriesClicked
        )
    }
}