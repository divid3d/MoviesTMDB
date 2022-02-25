@file:OptIn(ExperimentalFoundationApi::class)

package com.example.moviesapp.ui.screens.related

import android.os.Parcelable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
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
fun RelatedTvSeriesScreen(
    viewModel: RelatedTvSeriesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackButtonClicked: () -> Unit = { navigator.navigateUp() }
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
        onTvSeriesClicked = onTvSeriesClicked
    )
}

@Composable
fun RelatedTvSeriesScreenContent(
    uiState: RelatedTvSeriesScreenUiState,
    onBackButtonClicked: () -> Unit,
    onTvSeriesClicked: (tvSeriesId: Int) -> Unit
) {
    val tvSeries = uiState.tvSeries.collectAsLazyPagingItems()

    val appbarTitle = when (uiState.relationType) {
        RelationType.Similar -> "Podobne"
        RelationType.Recommended -> "Polecane"
    }

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
            }
        )

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