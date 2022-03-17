package com.example.moviesapp.ui.components.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.moviesapp.model.DetailPresentableItemState
import com.example.moviesapp.ui.theme.Size
import com.example.moviesapp.ui.theme.sizes

@Composable
fun DetailPresentableItem(
    presentableState: DetailPresentableItemState,
    modifier: Modifier = Modifier,
    size: Size = MaterialTheme.sizes.presentableItemSmall,
    selected: Boolean = false,
    showTitle: Boolean = true,
    showScore: Boolean = false,
    showAdult: Boolean = false,
    transformations: GraphicsLayerScope.() -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .width(size.width)
            .aspectRatio(size.ratio)
            .graphicsLayer {
                transformations()
            },
        shape = MaterialTheme.shapes.medium,
        border = if (selected) BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primary
        ) else null
    ) {
        when (presentableState) {
            is DetailPresentableItemState.Loading -> {
                LoadingPresentableItem(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is DetailPresentableItemState.Error -> {
                ErrorPresentableItem(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is DetailPresentableItemState.Result -> {
                ResultDetailPresentableItem(
                    modifier = Modifier.fillMaxSize(),
                    presentable = presentableState.presentable,
                    showScore = showScore,
                    showTitle = showTitle,
                    showAdult = showAdult,
                    onClick = onClick
                )
            }
        }
    }
}