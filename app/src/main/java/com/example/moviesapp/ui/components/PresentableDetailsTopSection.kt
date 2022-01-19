package com.example.moviesapp.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun PresentableDetailsTopSection(
    modifier: Modifier = Modifier,
    presentable: Presentable?,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val context = LocalContext.current

    val presentableState by derivedStateOf {
        presentable?.let { PresentableState.Result(it) } ?: PresentableState.Loading
    }

    Box(modifier = modifier) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = rememberImagePainter(
                data = presentable?.backdropUrl,
                builder = {
                    fadeIn(animationSpec = spring())
                    transformations(
                        BlurTransformation(
                            context = context,
                            radius = 16f,
                            sampling = 2f
                        )
                    )
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(128.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colors.background),
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 56.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                PresentableItem(
                    size = MaterialTheme.sizes.presentableItemBig,
                    showTitle = false,
                    presentableState = presentableState
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Column(modifier = Modifier.weight(1f)) {
                    content()
                }
            }
        }
    }
}