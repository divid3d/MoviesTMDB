package com.example.moviesapp.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.model.Network
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
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
        crossAxisAlignment = FlowCrossAxisAlignment.Start
    ) {
        networks.map { network ->
            NetworkChip(logoUrl = network.logoUrl)
        }
    }
}

@Composable
fun NetworkChip(
    modifier: Modifier = Modifier,
    logoUrl: String?
) {
    Image(
        modifier = Modifier.height(40.dp),
        painter = rememberImagePainter(
            data = logoUrl,
            builder = {
                fadeIn(animationSpec = spring())
            }
        ),
        contentDescription = null
    )
}