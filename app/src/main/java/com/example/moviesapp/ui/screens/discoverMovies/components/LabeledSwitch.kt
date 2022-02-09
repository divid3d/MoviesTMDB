package com.example.moviesapp.ui.screens.discoverMovies.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.ui.theme.spacing

@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = MaterialTheme.spacing.medium),
            text = "Tylko z plakatami"
        )
        Switch(checked = checked, onCheckedChange = onCheckedChanged)
    }
}