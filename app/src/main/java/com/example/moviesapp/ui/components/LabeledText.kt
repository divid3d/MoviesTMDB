package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.moviesapp.ui.theme.spacing

@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = label,
            style = TextStyle(color = Color.White)
        )
        Text(
            text = text,
            style = TextStyle(color = Color.White)
        )
    }
}