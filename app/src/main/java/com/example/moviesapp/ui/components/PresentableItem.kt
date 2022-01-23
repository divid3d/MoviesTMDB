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
import androidx.constraintlayout.compose.ConstraintLayout
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
    presentableState: PresentableItemState,
    showTitle: Boolean = true,
    showScore: Boolean = true,
    transformations: GraphicsLayerScope.() -> Unit = {},
    onClick: () -> Unit = {}
) {
    ConstraintLayout {
        val (content, score) = createRefs()

        Card(
            modifier = modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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
                    is PresentableItemState.Loading -> LoadingPresentableItem()
                    is PresentableItemState.Error -> ErrorPresentableItem()
                    is PresentableItemState.Result -> {
                        ResultPresentableItem(
                            presentableStateResult = state,
                            showTitle = showTitle,
                            onClick = onClick
                        )
                    }
                }
            }
        }

        if (presentableState is PresentableItemState.Result && presentableState.presentable.voteCount > 0 && showScore) {
            PresentableScoreItem(
                modifier = Modifier
                    .padding(end = MaterialTheme.spacing.small)
                    .constrainAs(score) {
                        top.linkTo(content.bottom)
                        bottom.linkTo(content.bottom)
                        end.linkTo(content.end)
                    },
                score = presentableState.presentable.voteAverage,
            )
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
                color = Color.White.copy(0.5f),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outline_no_photography_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White.copy(0.5f))
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
    modifier: Modifier = Modifier,
) {
    PosterPlaceholder(
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun ResultPresentableItem(
    modifier: Modifier = Modifier,
    presentableStateResult: PresentableItemState.Result,
    showTitle: Boolean = true,
    onClick: () -> Unit = {}
) {
    val presentable by derivedStateOf {
        presentableStateResult.presentable
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
    }
}

sealed class PresentableItemState {
    object Loading : PresentableItemState()
    object Error : PresentableItemState()
    data class Result(val presentable: Presentable) : PresentableItemState()
}

