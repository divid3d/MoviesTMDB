package com.example.moviesapp.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExitDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Are u sure u want to quit?")
        },
        text = {
            Text("Here is a text ")
        },
        confirmButton = {
            Button(
                onClick = onConfirmClick
            ) {
                Text("Exit")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancelClick
            ) {
                Text("Cancel")
            }
        }
    )
}