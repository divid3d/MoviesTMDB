package com.example.moviesapp.ui.screens.allMovies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class, kotlinx.coroutines.FlowPreview::class)
@Destination
@Composable
fun AllMoviesScreen(
    movieType: MovieType,
    navigator: DestinationsNavigator
) {
    val viewModel: AllMoviesViewModel = hiltViewModel()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val favouriteMoviesCount by viewModel.favouriteMoviesCount.collectAsState()

    val appbarTitle = when (movieType) {
        MovieType.Popular -> stringResource(R.string.all_movies_top_rated_label)
        MovieType.Upcoming -> stringResource(R.string.all_movies_upcoming_label)
        MovieType.TopRated -> stringResource(R.string.all_movies_top_rated_label)
        MovieType.Favourite -> stringResource(
            R.string.all_movies_favourites_label,
            favouriteMoviesCount
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBar(title = appbarTitle, action = {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        })
        PresentableGridSection(
            modifier = Modifier.fillMaxSize(),
            state = movies
        ) { movieId ->
            navigator.navigate(
                MovieDetailsScreenDestination(movieId)
            )
        }
    }
}