package com.example.moviesapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.moviesapp.R
import com.example.moviesapp.ui.theme.White300
import com.example.moviesapp.ui.theme.spacing

@Composable
fun FilterEmptyState(
    modifier: Modifier = Modifier,
    onFilterButtonClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            imageVector = Icons.Filled.Info,
            tint = White300,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = stringResource(R.string.filter_empty_info_text),
            color = White300
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedButton(onClick = onFilterButtonClicked) {
            Text(text = stringResource(R.string.filter_empty_button_change_filters_label))
        }
    }
}