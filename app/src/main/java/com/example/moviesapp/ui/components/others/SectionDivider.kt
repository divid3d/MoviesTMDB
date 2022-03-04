package com.example.moviesapp.ui.components.others

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.moviesapp.ui.theme.White300

@Composable
fun SectionDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        color = White300,
        thickness = Dp.Hairline
    )
}
