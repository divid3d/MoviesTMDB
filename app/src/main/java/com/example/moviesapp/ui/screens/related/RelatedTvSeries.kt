package com.example.moviesapp.ui.screens.related

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.TvSeriesRelationInfo
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun RelatedTvSeries(
    tvSeriesRelationInfo: TvSeriesRelationInfo,
    navigator: DestinationsNavigator
) {
    val viewModel: RelatedTvSeriesViewModel = hiltViewModel()
    val tvSeries = viewModel.tvSeries?.collectAsLazyPagingItems()

    val appbarTitle = when (tvSeriesRelationInfo.type) {
        RelationType.Similar -> "Podobne"
        RelationType.Recommended -> "Polecane"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(title = appbarTitle, action = {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "go back",
                    tint = MaterialTheme.colors.primary
                )
            }
        })
        tvSeries?.let { state ->
            PresentableGridSection(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.medium,
                ),
                state = state
            ) { tvSeriesId ->
                navigator.navigate(
                    TvSeriesDetailsScreenDestination(tvSeriesId)
                )
            }
        }
    }
}