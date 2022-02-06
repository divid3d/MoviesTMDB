package com.example.moviesapp.ui.components.dialogs

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.moviesapp.R

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    infoText: String?,
    onDismissRequest: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    ApplicationDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        infoText = infoText,
        confirmButton = {
            OutlinedButton(
                onClick = onConfirmClick
            ) {
                Text(text = stringResource(R.string.exit_dialog_confirm_button_label))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCancelClick
            ) {
                Text(text = stringResource(R.string.exit_dialog_cancel_button_label))
            }
        }
    )
}