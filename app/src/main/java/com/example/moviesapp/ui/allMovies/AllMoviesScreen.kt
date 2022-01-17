package com.example.moviesapp.ui.allMovies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.other.items
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.PresentableItem
import com.example.moviesapp.ui.components.PresentableState
import com.example.moviesapp.ui.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
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
    val moviesPageDataState = viewModel.moviesPagingDataFlow.collectAsLazyPagingItems()

    val appbarTitle by derivedStateOf {
        when (movieType) {
            MovieType.Popular -> "Popularne filmy"
            MovieType.Upcoming -> "Nadchodzące filmy"
            MovieType.TopRated -> "Najwyżej oceniane filmy"
            MovieType.Favourite -> "Ulubione filmy"
        }
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
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
            cells = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            items(moviesPageDataState) { movie ->
                movie?.let {
                    PresentableItem(presentableState = PresentableState.Result(movie)) {
                        navigator.navigate(
                            MovieDetailsScreenDestination(it.id)
                        )
                    }
                }
            }

            moviesPageDataState.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        items(12) {
                            PresentableItem(presentableState = PresentableState.Loading)
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        items(3) {
                            PresentableItem(presentableState = PresentableState.Loading)
                        }
                    }
//                    loadState.refresh is LoadState.Error -> {
//                        val e = moviesPageDataState.loadState.refresh as LoadState.Error
//
//                        item { MovieItem(presentableState = PresentableState.Error) }
//                    }
//                    loadState.append is LoadState.Error -> {
//                        val e = moviesPageDataState.loadState.refresh as LoadState.Error
//
//                        item { MovieItem(presentableState = PresentableState.Error) }
//                    }
                }
            }
        }
    }
}