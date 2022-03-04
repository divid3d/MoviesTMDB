package com.example.moviesapp.ui.components.lists

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.ProviderSource
import com.example.moviesapp.ui.components.chips.LogoChip
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun ProvidersSourceList(
    modifier: Modifier = Modifier,
    selectedProvidersSources: List<ProviderSource>,
    availableProvidersSources: List<ProviderSource>,
    onProviderSourceSelected: (ProviderSource) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = MaterialTheme.spacing.extraSmall,
        crossAxisSpacing = MaterialTheme.spacing.extraSmall
    ) {
        availableProvidersSources.map { providerSource ->
            val selected = providerSource in selectedProvidersSources

            LogoChip(
                logoPath = providerSource.logoPath,
                text = providerSource.providerName,
                selected = selected
            ) {
                onProviderSourceSelected(providerSource)
            }
        }
    }
}