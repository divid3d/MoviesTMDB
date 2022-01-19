package com.example.moviesapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.ui.theme.Orange
import com.example.moviesapp.ui.theme.spacing

@Composable
fun ScoreItem(
    modifier: Modifier = Modifier,
    score: Float,
    scoreRange: ClosedFloatingPointRange<Float> = 0f..10f,
) {
    val progress = score / scoreRange.run { endInclusive - start }

    val animatedProgress = remember { Animatable(0f) }

    val percent = (progress * 100).toInt()

    LaunchedEffect(progress) {
        animatedProgress.animateTo(progress)
    }

    val indicatorColor by animateColorAsState(
        targetValue = when (progress) {
            in 0f..0.3f -> Color.Red
            in 0.3f..0.5f -> Orange
            in 0.5f..0.7f -> Color.Yellow
            else -> Color.Green
        }
    )

    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        ) {
            append(percent.toString())
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
            )
        ) {
            append("%")
        }
    }

    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = MaterialTheme.colors.background
            )
            .wrapContentSize()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            ),
            textAlign = TextAlign.Center
        )
        CircularProgressIndicator(
            modifier = Modifier.matchParentSize(),
            progress = 1f,
            strokeWidth = 2.dp,
            color = indicatorColor.copy(alpha = 0.3f)
        )
        CircularProgressIndicator(
            modifier = Modifier.matchParentSize(),
            progress = animatedProgress.value,
            strokeWidth = 2.dp,
            color = indicatorColor
        )
    }
}