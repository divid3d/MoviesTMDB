package com.example.moviesapp.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun AdditionalInfoText(
    modifier: Modifier = Modifier,
    infoTexts: List<String>
) {
    val text = infoTexts.joinToString(separator = " · ")

    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            color = Color.White.copy(0.5f),
            fontSize = 12.sp
        )
    )
}