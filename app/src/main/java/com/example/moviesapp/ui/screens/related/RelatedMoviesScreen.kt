package com.example.moviesapp.ui.screens.related

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableGridSection
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.RelatedMoviesScreenDestination
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
    navigator: DestinationsNavigator,
    navBackStackEntry: NavBackStackEntry
) {
    val navArgs: RelatedMoviesScreenArgs by derivedStateOf {
        RelatedMoviesScreenDestination.argsFrom(navBackStackEntry)
    }
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val appbarTitle = when (navArgs.type) {
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

        PresentableGridSection(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.medium,
            ),
            state = movies
        ) { movieId ->
            val destination = MovieDetailsScreenDestination(
                movieId = movieId,
                startRoute = navArgs.startRoute
            )

            navigator.navigate(destination)
        }
    }
}