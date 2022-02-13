package com.example.moviesapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.moviesapp.model.MovieWatchProviderType
import com.example.moviesapp.model.MovieWatchProviders
import com.example.moviesapp.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesWatchProvidersSection(
    modifier: Modifier = Modifier,
    watchProviders: MovieWatchProviders,
    title: String
) {
    val stream = watchProviders.flatrate?.sortedBy { provider ->
        provider.displayPriority
    } ?: emptyList()
    val rent = watchProviders.rent?.sortedBy { provider ->
        provider.displayPriority
    } ?: emptyList()
    val buy = watchProviders.buy?.sortedBy { provider ->
        provider.displayPriority
    } ?: emptyList()

    val availableTypes: List<MovieWatchProviderType> = buildList {
        if (stream.isNotEmpty()) add(MovieWatchProviderType.Stream)
        if (rent.isNotEmpty()) add(MovieWatchProviderType.Rent)
        if (buy.isNotEmpty()) add(MovieWatchProviderType.Buy)
    }

    var selectedType by remember(availableTypes) {
        mutableStateOf(availableTypes.firstOrNull())
    }

    val selectedProviders by remember(selectedType) {
        mutableStateOf(
            when (selectedType) {
                MovieWatchProviderType.Stream -> stream
                MovieWatchProviderType.Buy -> buy
                MovieWatchProviderType.Rent -> rent
                else -> null
            }
        )
    }

    if (availableTypes.isNotEmpty()) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionLabel(
                    modifier = Modifier.weight(1f),
                    text = title
                )

                selectedType?.let { currentType ->
                    MoviesWatchProvidersTypeButton(
                        selectedType = currentType,
                        availableTypes = availableTypes
                    ) { type ->
                        selectedType = type
                    }
                }
            }

            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
            ) {
                selectedProviders?.let { providers ->
                    items(providers, key = { provider -> provider.providerId }) { provider ->
                        LogoChip(
                            modifier = Modifier.animateItemPlacement(),
                            logoPath = provider.logoPath,
                            text = provider.providerName
                        )
                    }
                }
            }
        }
    }

}

