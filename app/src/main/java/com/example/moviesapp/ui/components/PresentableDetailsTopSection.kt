package com.example.moviesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.moviesapp.model.Image
import com.example.moviesapp.model.Presentable
import com.example.moviesapp.model.PresentableItemState
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun PresentableDetailsTopSection(
    modifier: Modifier = Modifier,
    presentable: Presentable?,
    backdrops: List<Image> = emptyList(),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val presentableItemState by derivedStateOf {
        presentable?.let { PresentableItemState.Result(it) } ?: PresentableItemState.Loading
    }

    val availableBackdropPaths by remember(backdrops) {
        mutableStateOf(
            backdrops.map { backdrops -> backdrops.filePath }
        )
    }


    Box(modifier = modifier.clip(RectangleShape)) {
        AnimatedBackdrops(
            modifier = Modifier.matchParentSize(),
            paths = availableBackdropPaths
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
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                val (presentableRef, contentRef) = createRefs()

                PresentableItem(
                    modifier = Modifier.constrainAs(presentableRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                    size = MaterialTheme.sizes.presentableItemBig,
                    showScore = true,
                    showTitle = false,
                    showAdult = true,
                    presentableState = presentableItemState
                )

                Column(
                    modifier = Modifier
                        .constrainAs(contentRef) {
                            start.linkTo(presentableRef.end)
                            end.linkTo(parent.end)
                            top.linkTo(presentableRef.top)
                            bottom.linkTo(presentableRef.bottom)

                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        }
                        .padding(start = MaterialTheme.spacing.medium)
                ) {
                    content()
                }
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        }
    }
}