package com.example.moviesapp.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.moviesapp.model.MovieWatchProviderType

@Composable
fun MovieWatchProviderTypeDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit = {},
    availableTypes: List<MovieWatchProviderType>,
    selectedType: MovieWatchProviderType,
    onTypeSelected: (MovieWatchProviderType) -> Unit = {}
) {
    val items = availableTypes.map { type -> type to type.getLabelResId() }

    if (items.isNotEmpty()) {
        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            items.forEach { (type, labelResId) ->
                DropdownMenuItem(
                    onClick = { onTypeSelected(type) })
                {
                    Text(
                        text = stringResource(labelResId),
                        color = if (type == selectedType) MaterialTheme.colors.primary else Color.White
                    )
                }
            }
        }
    }
}