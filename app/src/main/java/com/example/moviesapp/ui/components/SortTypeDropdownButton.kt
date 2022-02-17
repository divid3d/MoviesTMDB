package com.example.moviesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.R
import com.example.moviesapp.model.SortType

@Composable
fun SortTypeDropdownButton(
    modifier: Modifier = Modifier,
    selectedType: SortType,
    onTypeSelected: (SortType) -> Unit = {}
) {
    var showSortTypeDropdown by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(
            onClick = {
                showSortTypeDropdown = true
            }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_baseline_sort_24),
                contentDescription = "sort type",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
        }

        SortTypeDropdown(
            expanded = showSortTypeDropdown,
            onDismissRequest = {
                showSortTypeDropdown = false
            },
            selectedSortType = selectedType,
            onSortTypeSelected = { type ->
                showSortTypeDropdown = false

                if (type != selectedType) {
                    onTypeSelected(type)
                }
            }
        )
    }
}