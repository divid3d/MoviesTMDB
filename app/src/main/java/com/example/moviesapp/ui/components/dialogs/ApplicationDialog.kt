package com.example.moviesapp.ui.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.moviesapp.R

@Composable
fun ApplicationDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    infoText: String? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null
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
        text = { infoText?.let { text -> Text(text = text) } },
        confirmButton = { confirmButton?.invoke() },
        dismissButton = { dismissButton?.invoke() }
    )
}