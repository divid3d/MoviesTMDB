package com.example.moviesapp.ui.components.lists

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.moviesapp.model.Review
import com.example.moviesapp.other.hasItems
import com.example.moviesapp.ui.components.items.ReviewItem
import com.example.moviesapp.ui.components.items.ReviewItemPlaceholder
import com.example.moviesapp.ui.theme.spacing


@Composable
fun ReviewsList(
    reviews: LazyPagingItems<Review>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(MaterialTheme.spacing.large)
) {
    Crossfade(
        modifier = modifier,
        targetState = reviews.hasItems()
    ) { hasReviews ->
        if (hasReviews) {
            ReviewsListResult(
                modifier = Modifier.fillMaxSize(),
                reviews = reviews,
                contentPadding = contentPadding,
                arrangement = arrangement
            )
        } else {
            ReviewsListPlaceholder(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                arrangement = arrangement
            )
        }
    }
}

@Composable
private fun ReviewsListResult(
    reviews: LazyPagingItems<Review>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(MaterialTheme.spacing.large)

) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = arrangement
    ) {
        itemsIndexed(reviews) { index, review ->
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

@Composable
private fun ReviewsListPlaceholder(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(MaterialTheme.spacing.large)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = arrangement
    ) {
        items(10) { index ->
            val height: Int = rememberSaveable(index) {
                (150..200).random()
            }

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
                ReviewItemPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(height.dp)
                        .align(alignment),
                    shape = shape
                )
            }
        }
    }
}