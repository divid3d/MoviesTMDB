package com.example.moviesapp.ui.screens.person.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.ui.theme.spacing

@Composable
fun ExternalIdsSection(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.default),
    externalIds: List<ExternalId>,
    onExternalIdClick: (ExternalId) -> Unit = {}
) {
    if (externalIds.isNotEmpty()) {
        LazyRow(
            modifier = modifier
                .wrapContentHeight()
                .background(color = MaterialTheme.colors.surface),
            contentPadding = contentPadding,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(externalIds) { id ->
                IdChip(drawableRes = id.drawableRes) {
                    onExternalIdClick(id)
                }
            }
        }
    }
}

@Composable
fun IdChip(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )
    }
}