package com.example.moviesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.moviesapp.R

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onDismissRequest: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.ic_tmdb_logo_long),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        },
        text = {
            Text(text = errorMessage ?: stringResource(R.string.error_dialog_default_text))
        },
        confirmButton = {
            OutlinedButton(
                onClick = onConfirmClick
            ) {
                Text(text = stringResource(R.string.exit_dialog_confirm_button_label))
            }
        }
    )
}