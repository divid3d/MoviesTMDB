package com.example.moviesapp.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.Size
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PresentableItem(
    modifier: Modifier = Modifier,
    size: Size = MaterialTheme.sizes.presentableItemSmall,
    presentableState: PresentableState,
    showTitle: Boolean = true,
    transformations: GraphicsLayerScope.() -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(size.width)
            .height(size.height)
            .graphicsLayer {
                transformations()
            },
        shape = MaterialTheme.shapes.medium,
        backgroundColor = Color.Transparent
    ) {
        Crossfade(modifier = Modifier.fillMaxSize(), targetState = presentableState) { state ->
            when (state) {
                is PresentableState.Loading -> LoadingPresentableItem()
                is PresentableState.Error -> ErrorPresentableItem()
                is PresentableState.Result -> {
                    ResultPresentableItem(
                        presentableStateResult = state,
                        showTitle = showTitle,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun NoPhotoPresentableItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outline_no_photography_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
fun ErrorPresentableItem(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        //TODO change error icon
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = null
        )
    }
}

@Composable
fun LoadingPresentableItem(
    modifier: Modifier = Modifier
) {
    PosterPlaceholder(
        modifier = modifier
            .width(130.dp)
            .height(200.dp)
    )
}

@Composable
fun ResultPresentableItem(
    modifier: Modifier = Modifier,
    showTitle: Boolean = true,
    presentableStateResult: PresentableState.Result,
    onClick: () -> Unit = {}
) {
    val presentable by derivedStateOf {
        presentableStateResult.presentable
    }

    val scoreItemVisible by derivedStateOf {
        presentable.voteCount > 0
    }

    val hasPoster by derivedStateOf {
        presentable.posterPath != null
    }

    Box(modifier = modifier
        .fillMaxSize()
        .clickable { onClick() }
    ) {
        if (hasPoster) {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = rememberImagePainter(
                    data = presentable.posterUrl,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        } else {
            NoPhotoPresentableItem()
        }

        if (showTitle) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Black500)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(MaterialTheme.spacing.extraSmall),
                    text = presentable.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
            }
        }

        if (scoreItemVisible) {
            ScoreItem(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.spacing.extraSmall),
                score = presentable.voteAverage
            )
        }
    }
}

sealed class PresentableState {
    object Loading : PresentableState()
    object Error : PresentableState()
    data class Result(val presentable: Presentable) : PresentableState()
}

