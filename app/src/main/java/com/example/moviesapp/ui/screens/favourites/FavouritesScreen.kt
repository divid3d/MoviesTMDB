package com.example.moviesapp.ui.screens.favourites

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.FavouriteType
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
        favourites.itemCount > 0
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

@Composable
fun FavouriteEmptyState(
    modifier: Modifier,
    type: FavouriteType,
    onButtonClick: () -> Unit = {}
) {
    @StringRes
    val infoTextResId = when (type) {
        FavouriteType.Movie -> R.string.favourite_empty_movies_info_text
        FavouriteType.TvSeries -> R.string.favourite_empty_tv_series_info_text
    }

    @StringRes
    val buttonLabelResId = when (type) {
        FavouriteType.Movie -> R.string.favourite_empty_navigate_to_movies_button_label
        FavouriteType.TvSeries -> R.string.favourite_empty_navigate_to_tv_series_button_label
    }

    @DrawableRes
    val icon = when (type) {
        FavouriteType.Movie -> R.drawable.ic_outline_movie_24
        FavouriteType.TvSeries -> R.drawable.ic_outline_tv_24
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        Text(
            text = stringResource(infoTextResId)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedButton(onClick = onButtonClick) {
            Text(
                text = stringResource(buttonLabelResId)
            )
        }
    }
}