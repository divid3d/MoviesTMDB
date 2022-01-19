package com.example.moviesapp.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Label(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        text = label,
        style = TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    )
}