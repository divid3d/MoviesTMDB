package com.example.moviesapp.ui.screens.discoverMovies.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.moviesapp.model.SortType

@Composable
fun SortTypeDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit = {},
    selectedSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit = {}
) {
    val items = SortType.values().map { type -> type to type.getLabel() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            items.forEach { (type, labelResId) ->
                DropdownMenuItem(
                    onClick = { onSortTypeSelected(type) })
                {
                    Text(
                        text = stringResource(labelResId),
                        color = if (type == selectedSortType) MaterialTheme.colors.primary else Color.White
                    )
                }
            }
        }
    }
}