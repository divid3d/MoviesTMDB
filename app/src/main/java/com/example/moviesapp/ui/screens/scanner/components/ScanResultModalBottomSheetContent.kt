package com.example.moviesapp.ui.screens.scanner.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.ui.screens.scanner.ScanResult
import com.example.moviesapp.ui.theme.spacing

@Composable
fun ScanResultModalBottomSheetContent(
    scanResult: ScanResult?,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onRejectClicked: () -> Unit = {},
    onAcceptClicked: () -> Unit = {}
) {
    val scannedText = if (scanResult is ScanResult.Success) scanResult.text else ""

    Column(
        modifier = modifier.padding(top = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier.width(64.dp),
            color = Color.White.copy(0.3f),
            thickness = 4.dp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close filter",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        Text(text = scannedText, fontSize = 32.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                onClick = onRejectClicked
            ) {
                Text(
                    text = stringResource(R.string.scanner_reject_button_label),
                    color = Color.White
                )
            }
            OutlinedButton(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                onClick = onAcceptClicked
            ) {
                Text(
                    text = stringResource(R.string.scanner_accept_button_label),
                    color = Color.White
                )
            }
        }
    }
}