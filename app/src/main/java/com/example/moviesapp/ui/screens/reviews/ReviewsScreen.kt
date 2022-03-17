package com.example.moviesapp.ui.screens.reviews

import android.os.Parcelable
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.ui.components.items.ReviewItem
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.screens.destinations.FavouritesScreenDestination
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalAnimationApi::class)
object ReviewsScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            MoviesScreenDestination.route,
            TvScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            MoviesScreenDestination.route,
            TvScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }
}

@Parcelize
data class ReviewsScreenNavArgs(
    val startRoute: String,
    val mediaId: Int,
    val type: MediaType
) : Parcelable

@Destination(
    navArgsDelegate = ReviewsScreenNavArgs::class,
    style = ReviewsScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.ReviewsScreen(
    viewModel: ReviewsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onCloseClicked: () -> Unit = {
        navigator.popBackStack(uiState.startRoute, inclusive = false)
    }

    ReviewsScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onCloseClicked = onCloseClicked
    )
}

@Composable
fun ReviewsScreenContent(
    uiState: ReviewsScreenUiState,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    val reviewsLazyItems = uiState.reviews.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        AppBar(
            title = stringResource(R.string.reviews_screen_appbar_title),
            action = {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "go back",
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            trailing = {
                Row(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                    IconButton(
                        onClick = onCloseClicked
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "close",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = MaterialTheme.spacing.medium,
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.large
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            itemsIndexed(reviewsLazyItems) { index, review ->
                if (review != null) {
                    val alignment = if (index % 2 == 0) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    }

                    val shape = if (index % 2 == 0) {
                        MaterialTheme.shapes.medium.copy(bottomStart = CornerSize(0.dp))
                    } else {
                        MaterialTheme.shapes.medium.copy(bottomEnd = CornerSize(0.dp))
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        ReviewItem(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(alignment),
                            review = review,
                            shape = shape
                        )
                    }
                }
            }
        }
    }
}