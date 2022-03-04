package com.example.moviesapp.ui.components.others

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.moviesapp.ui.theme.spacing

@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = MaterialTheme.spacing.medium),
            text = label,
            fontSize = 14.sp
        )
        Switch(checked = checked, onCheckedChange = onCheckedChanged)
    }
}