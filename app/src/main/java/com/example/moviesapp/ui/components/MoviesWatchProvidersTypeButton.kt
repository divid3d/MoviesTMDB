package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.moviesapp.model.MovieWatchProviderType

@Composable
fun MoviesWatchProvidersTypeButton(
    modifier: Modifier = Modifier,
    availableTypes: List<MovieWatchProviderType>,
    selectedType: MovieWatchProviderType,
    onTypeSelected: (MovieWatchProviderType) -> Unit = {}
) {
    var showMovieWatchProviderTypeDropdown by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopEnd)) {
        TextButton(
            onClick = { showMovieWatchProviderTypeDropdown = true }
        ) {
            Text(
                text = stringResource(selectedType.getLabelResId()),
                color = Color.White,
                fontSize = 12.sp
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null
            )
        }

        MovieWatchProviderTypeDropdown(
            expanded = showMovieWatchProviderTypeDropdown,
            availableTypes = availableTypes,
            onDismissRequest = {
                showMovieWatchProviderTypeDropdown = false
            },
            selectedType = selectedType,
            onTypeSelected = { type ->
                showMovieWatchProviderTypeDropdown = false

                if (type != selectedType) {
                    onTypeSelected(type)
                }
            }
        )
    }
}