package com.example.moviesapp.ui.screens.related.movies

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class RelatedMoviesScreenUiState(
    val relationType: RelationType,
    val movies: Flow<PagingData<Movie>>,
    val startRoute: String
) {
    companion object {
        fun getDefault(relationType: RelationType): RelatedMoviesScreenUiState {
            return RelatedMoviesScreenUiState(
                relationType = relationType,
                movies = emptyFlow(),
                startRoute = MoviesScreenDestination.route
            )
        }
    }
}