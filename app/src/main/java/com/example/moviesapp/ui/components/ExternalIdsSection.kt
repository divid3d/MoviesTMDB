package com.example.moviesapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.ui.theme.spacing

@Composable
fun ExternalIdsSection(
    modifier: Modifier = Modifier,
    externalIds: List<ExternalId>,
    onExternalIdClick: (ExternalId) -> Unit = {}
) {
    if (externalIds.isNotEmpty()) {
        Row(
            modifier = modifier.wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            externalIds.map { id ->
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
    Box(modifier = modifier
        .clip(CircleShape)
        .clickable { onClick() }
        .padding(MaterialTheme.spacing.extraSmall)
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )
    }

}