package com.example.moviesapp.ui.components.others

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String?,
    scrollState: ScrollState? = null,
    transparentScrollValueLimit: Float? = null,
    backgroundColor: Color = Color.Black,
    action: @Composable () -> Unit = {},
    trailing: @Composable () -> Unit = {}
) {
    val alphaDelta = 1f - backgroundColor.alpha

    val currentScrollValue = scrollState?.value

    val alpha = if (currentScrollValue != null && transparentScrollValueLimit != null) {
        (backgroundColor.alpha + (currentScrollValue / transparentScrollValueLimit) * alphaDelta).coerceIn(
            backgroundColor.alpha,
            1f
        )
    } else {
        backgroundColor.alpha
    }

    Row(
        modifier = modifier
            .background(backgroundColor.copy(alpha))
            .fillMaxWidth()
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            action()
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            title?.let {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = MaterialTheme.spacing.medium),
                    text = it,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            trailing()
        }
    }
}