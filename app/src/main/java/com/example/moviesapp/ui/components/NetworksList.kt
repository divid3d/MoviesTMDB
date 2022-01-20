package com.example.moviesapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.model.Network
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

@Composable
fun NetworksList(
    modifier: Modifier = Modifier,
    networks: List<Network>
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = MaterialTheme.spacing.small,
        crossAxisSpacing = MaterialTheme.spacing.medium,
        mainAxisAlignment = MainAxisAlignment.Start,
        crossAxisAlignment = FlowCrossAxisAlignment.Start,
        lastLineMainAxisAlignment = FlowMainAxisAlignment.Start
    ) {
        networks.map { network ->
            NetworkChip(
                logoUrl = network.logoUrl
            )
        }
    }
}

@Composable
fun NetworkChip(
    modifier: Modifier = Modifier,
    logoUrl: String?
) {
    Image(
        modifier = modifier
            .height(40.dp)
            .requiredWidthIn(min = 40.dp, max = 100.dp)
            .wrapContentWidth()
            .background(
                color = Color.White.copy(0.3f),
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = MaterialTheme.shapes.small
            )
            .padding(MaterialTheme.spacing.small)
            .animateContentSize(),
        painter = rememberImagePainter(
            data = logoUrl,
            builder = {
                fadeIn(animationSpec = spring())
            }
        ),
        contentDescription = null
    )
}