package com.example.moviesapp.ui.components.selectors

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.ui.theme.spacing

@Composable
fun FavouriteTypeSelector(
    selected: FavouriteType,
    modifier: Modifier = Modifier,
    onSelected: (FavouriteType) -> Unit = {}
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            )
            .clip(shape = MaterialTheme.shapes.small),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FavouriteType.values().map { type ->
            FavouriteTypeButton(
                modifier = Modifier.weight(1f),
                type = type,
                selected = type == selected,
                onClick = { onSelected(type) }
            )
        }
    }
}

@Composable
fun FavouriteTypeButton(
    type: FavouriteType,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colors.primary
        } else Color.Transparent
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) {
            Color.White
        } else MaterialTheme.colors.primary
    )

    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(MaterialTheme.spacing.small),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(type.getLabelResourceId()),
            color = textColor, fontWeight = FontWeight.Bold
        )
    }
}