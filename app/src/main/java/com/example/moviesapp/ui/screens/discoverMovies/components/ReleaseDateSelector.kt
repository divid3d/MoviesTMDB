package com.example.moviesapp.ui.screens.discoverMovies.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.moviesapp.other.createDateDialog
import com.example.moviesapp.other.formatted
import com.example.moviesapp.ui.theme.spacing
import java.util.*

@Composable
fun ReleaseDateSelector(
    modifier: Modifier = Modifier,
    fromDate: Date?,
    toDate: Date?,
    onFromDateChanged: (Date) -> Unit = {},
    onToDateChanged: (Date) -> Unit = {}
) {
    val context = LocalContext.current

    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .clickable {
                    createDateDialog(
                        context = context,
                        initialDate = fromDate,
                        onDateSelected = onFromDateChanged,
                        maxDate = toDate
                    ).show()
                }
                .border(
                    width = 1.dp,
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colors.primary
                )
                .padding(MaterialTheme.spacing.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = fromDate?.formatted() ?: "Data od"
            )
        }
        Box(
            modifier = Modifier
                .clickable {
                    createDateDialog(
                        context = context,
                        initialDate = toDate,
                        onDateSelected = onToDateChanged,
                        minDate = fromDate
                    ).show()
                }
                .border(
                    width = 1.dp,
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colors.primary
                )
                .padding(MaterialTheme.spacing.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = toDate?.formatted() ?: "Data do"
            )
        }
    }
}