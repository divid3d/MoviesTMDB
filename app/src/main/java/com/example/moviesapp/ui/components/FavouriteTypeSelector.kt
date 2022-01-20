package com.example.moviesapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.example.moviesapp.model.FavouriteType

@Composable
fun FavouriteTypeSelector(
    modifier: Modifier = Modifier,
    selected: FavouriteType,
    onSelected: (FavouriteType) -> Unit = {}
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        FavouriteType.values().map { type ->
            FavouriteTypeButton(
                modifier = Modifier.weight(1f),
                type = type,
                selected = type == selected
            ) {
                onSelected(type)
            }
        }
    }
}

@Composable
fun FavouriteTypeButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    type: FavouriteType,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(targetValue = if (selected) Color.White else Color.Transparent)
    val textColor by animateColorAsState(targetValue = if (selected) Color.Black else Color.White)

    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(text = stringResource(type.getLabelResourceId()), style = TextStyle(color = textColor))
    }
}