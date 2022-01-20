package com.example.moviesapp.ui.screens.favourites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.ui.components.FavouriteTypeSelector
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FavouritesScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: FavouritesViewModel = hiltViewModel()

    val selectedFavouriteType by viewModel.selectedFavouriteType.collectAsState()
    val favourites = viewModel.favourites.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        FavouriteTypeSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            selected = selectedFavouriteType,
            onSelected = { type -> viewModel.onFavouriteTypeSelected(type) }
        )
        PresentableGridSection(
            modifier = Modifier.fillMaxSize(),
            state = favourites
        ) { id ->
            navigator.navigate(
                when (selectedFavouriteType) {
                    FavouriteType.Movie -> MovieDetailsScreenDestination(id)
                    FavouriteType.TvSeries -> TvSeriesDetailsScreenDestination(id)
                }
            )
        }
    }
}