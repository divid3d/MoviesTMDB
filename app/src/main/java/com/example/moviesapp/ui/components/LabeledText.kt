package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.moviesapp.ui.theme.spacing

@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    spacing: Dp = MaterialTheme.spacing.default
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Label(label = label)
        Text(
            text = text,
            color = Color.White
        )
    }
}