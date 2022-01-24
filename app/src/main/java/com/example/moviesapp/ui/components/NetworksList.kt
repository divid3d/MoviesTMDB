package com.example.moviesapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.model.Network
import com.example.moviesapp.ui.theme.White300
import com.example.moviesapp.ui.theme.White500
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
    val painter = rememberImagePainter(
        data = logoUrl,
        builder = {
            crossfade(true)
        }
    )

    Image(
        modifier = modifier
            .height(40.dp)
            .width(60.dp)
            .background(
                color = White300,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = White500,
                shape = MaterialTheme.shapes.small
            )
            .padding(MaterialTheme.spacing.small)
            .animateContentSize(),
        painter = painter,
        contentDescription = null
    )

}