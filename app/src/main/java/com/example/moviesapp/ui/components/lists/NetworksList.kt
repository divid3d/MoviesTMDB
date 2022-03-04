package com.example.moviesapp.ui.components.lists

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.Network
import com.example.moviesapp.ui.components.chips.LogoChip
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
            LogoChip(
                logoPath = network.logoPath,
                text = network.name
            )
        }
    }
}

