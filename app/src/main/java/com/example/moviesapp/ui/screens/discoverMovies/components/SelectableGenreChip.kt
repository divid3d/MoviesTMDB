package com.example.moviesapp.ui.screens.discoverMovies.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.ui.theme.spacing

@Composable
fun SelectableGenreChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor by animateColorAsState(targetValue = if (selected) MaterialTheme.colors.primary else Color.Black)

    Box(
        modifier = modifier
            .background(shape = RoundedCornerShape(50f), color = backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary.copy(0.5f),
                shape = RoundedCornerShape(50f)
            )
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            )
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}