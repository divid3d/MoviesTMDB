package com.example.moviesapp.ui.screens.favourites

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.ui.components.others.FavouriteEmptyState
import com.example.moviesapp.ui.components.sections.PresentableGridSection
import com.example.moviesapp.ui.components.selectors.FavouriteTypeSelector
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AnimatedVisibilityScope.FavouritesScreen(
    viewModel: FavouritesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onFavouriteTypeSelected: (type: FavouriteType) -> Unit = viewModel::onFavouriteTypeSelected
    val onFavouriteClicked: (favouriteId: Int) -> Unit = { id ->
        val destination = when (uiState.selectedFavouriteType) {
            FavouriteType.Movie -> {
                MovieDetailsScreenDestination(
                    movieId = id,
                    startRoute = FavouritesScreenDestination.route
                )
            }

            FavouriteType.TvSeries -> {
                TvSeriesDetailsScreenDestination(
                    tvSeriesId = id,
                    startRoute = FavouritesScreenDestination.route
                )
            }
        }

        navigator.navigate(destination)
    }
    val onNavigateToMoviesButtonClicked: () -> Unit = {
        navigator.navigate(MoviesScreenDestination) {
            popUpTo(MoviesScreenDestination.route) {
                inclusive = true
            }
        }
    }
    val onNavigateToTvSeriesButtonClicked: () -> Unit = {
        navigator.navigate(TvScreenDestination) {
            popUpTo(MoviesScreenDestination.route) {
                inclusive = false
            }
        }
    }

    FavouriteScreenContent(
        uiState = uiState,
        onFavouriteTypeSelected = onFavouriteTypeSelected,
        onFavouriteClicked = onFavouriteClicked,
        onNavigateToMoviesButtonClicked = onNavigateToMoviesButtonClicked,
        onNavigateToTvSeriesButtonClicked = onNavigateToTvSeriesButtonClicked
    )
}

@Composable
fun FavouriteScreenContent(
    uiState: FavouritesScreenUiState,
    onFavouriteTypeSelected: (type: FavouriteType) -> Unit,
    onFavouriteClicked: (favouriteId: Int) -> Unit,
    onNavigateToMoviesButtonClicked: () -> Unit,
    onNavigateToTvSeriesButtonClicked: () -> Unit
) {
    val favouritesLazyItems = uiState.favourites.collectAsLazyPagingItems()
    val notEmpty = favouritesLazyItems.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .statusBarsPadding()
    ) {
        FavouriteTypeSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            selected = uiState.selectedFavouriteType,
            onSelected = onFavouriteTypeSelected
        )
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = notEmpty
        ) { notEmpty ->
            if (notEmpty) {
                PresentableGridSection(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = MaterialTheme.spacing.medium,
                        start = MaterialTheme.spacing.small,
                        end = MaterialTheme.spacing.small,
                        bottom = MaterialTheme.spacing.large
                    ),
                    state = favouritesLazyItems,
                    showRefreshLoading = false,
                    onPresentableClick = onFavouriteClicked
                )
            } else {
                FavouriteEmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.medium)
                        .padding(top = MaterialTheme.spacing.extraLarge),
                    type = uiState.selectedFavouriteType,
                    onButtonClick = when (uiState.selectedFavouriteType) {
                        FavouriteType.Movie -> onNavigateToMoviesButtonClicked
                        FavouriteType.TvSeries -> onNavigateToTvSeriesButtonClicked
                    }
                )
            }
        }
    }
}