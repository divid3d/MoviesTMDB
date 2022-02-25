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
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelatedMoviesScreenArgs(
    val movieId: Int,
    val type: RelationType,
    val startRoute: String
) : Parcelable

@Destination(navArgsDelegate = RelatedMoviesScreenArgs::class)
@Composable
fun RelatedMoviesScreen(
    viewModel: RelatedMoviesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackButtonClicked: () -> Unit = { navigator.navigateUp() }
    val onMovieClicked: (movieId: Int) -> Unit = { id ->
        val destination = MovieDetailsScreenDestination(
            movieId = id,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }

    RelatedMoviesScreenContent(
        uiState = uiState,
        onBackButtonClicked = onBackButtonClicked,
        onMovieClicked = onMovieClicked
    )
}

@Composable
fun RelatedMoviesScreenContent(
    uiState: RelatedMoviesScreenUiState,
    onBackButtonClicked: () -> Unit,
    onMovieClicked: (movieId: Int) -> Unit
) {
    val movies = uiState.movies.collectAsLazyPagingItems()

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
            state = movies,
            onPresentableClick = onMovieClicked
        )
    }
}