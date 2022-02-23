package com.example.moviesapp.ui.screens.reviews

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.ReviewItem
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewsScreenNavArgs(
    val mediaId: Int,
    val type: MediaType
) : Parcelable

@Destination(navArgsDelegate = ReviewsScreenNavArgs::class)
@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }

    ReviewsScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked
    )
}

@Composable
fun ReviewsScreenContent(
    uiState: ReviewsScreenUiState,
    onBackClicked: () -> Unit
) {
    val reviewsLazyItems = uiState.reviews.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
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
            })

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            itemsIndexed(reviewsLazyItems) { index, review ->
                if (review != null) {
                    val alignment = if (index % 2 == 0) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        ReviewItem(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(alignment),
                            review = review
                        )
                    }
                }
            }
        }
    }
}