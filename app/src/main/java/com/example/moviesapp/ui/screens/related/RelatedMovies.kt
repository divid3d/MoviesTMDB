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
import com.example.moviesapp.model.MovieRelationInfo
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun RelatedMovies(
    viewModel: RelatedMoviesViewModel = hiltViewModel(),
    movieRelationInfo: MovieRelationInfo,
    navigator: DestinationsNavigator
) {
    val movies = viewModel.movies?.collectAsLazyPagingItems()

    val appbarTitle = when (movieRelationInfo.type) {
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
        movies?.let { state ->
            PresentableGridSection(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.medium,
                ),
                state = state
            ) { movieId ->
                navigator.navigate(
                    MovieDetailsScreenDestination(movieId)
                )
            }
        }
    }
}