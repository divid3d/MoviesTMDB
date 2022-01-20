package com.example.moviesapp.ui.screens.favourites

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.ui.components.FavouriteEmptyState
import com.example.moviesapp.ui.components.FavouriteTypeSelector
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.*
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

    val notEmpty by derivedStateOf {
        favourites.run {
            loadState.refresh is LoadState.Loading || itemCount > 0
        }
    }

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
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = notEmpty to selectedFavouriteType
        ) { (notEmpty, type) ->
            when {
                notEmpty -> {
                    PresentableGridSection(
                        modifier = Modifier.fillMaxSize(),
                        state = favourites
                    ) { id ->
                        navigator.navigate(
                            when (selectedFavouriteType) {
                                FavouriteType.Movie -> MovieDetailsScreenDestination(
                                    movieId = id,
                                    startRoute = FavouritesScreenDestination.route
                                )
                                FavouriteType.TvSeries -> TvSeriesDetailsScreenDestination(
                                    tvSeriesId = id,
                                    startRoute = FavouritesScreenDestination.route
                                )
                            }
                        )
                    }
                }

                type == FavouriteType.Movie -> {
                    FavouriteEmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.medium),
                        type = type,
                        onButtonClick = {
                            navigator.navigate(MoviesScreenDestination)
                        }
                    )
                }

                type == FavouriteType.TvSeries -> {
                    FavouriteEmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.medium),
                        type = type,
                        onButtonClick = {
                            navigator.navigate(TvScreenDestination)
                        }
                    )
                }
            }
        }
    }
}