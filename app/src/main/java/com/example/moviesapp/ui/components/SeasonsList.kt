package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SeasonsList(
    modifier: Modifier = Modifier,
    seasonNumbers: List<Int>,
    selectedSeason: Int?,
    onSeasonClick: (Int) -> Unit = {}
) {
    Column(modifier = modifier) {
        LazyRow {
            items(seasonNumbers) { number ->
                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (number == selectedSeason) Color.White.copy(
                            alpha = 0.5f
                        ) else Color.Transparent
                    ),
                    onClick = { onSeasonClick(number) }) {
                    Text(text = "Sezon $number")
                }
            }
        }
    }
}