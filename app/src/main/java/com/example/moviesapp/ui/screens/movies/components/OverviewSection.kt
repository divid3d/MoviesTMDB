package com.example.moviesapp.ui.screens.movies.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.ui.theme.spacing

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    overview: String
) {
    var showReadMore by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            modifier = Modifier.animateContentSize(),
            text = overview,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp
            ),
            maxLines = if (expanded) Int.MAX_VALUE else 6,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (!expanded) {
                    showReadMore = textLayoutResult.hasVisualOverflow
                }
            }
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.End),
            visible = showReadMore,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (showReadMore) {
                ReadMoreButton(
                    modifier = Modifier.animateContentSize(),
                    expanded = expanded
                ) {
                    expanded = !expanded
                }
            }
        }
    }
}

@Composable
fun ReadMoreButton(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit = {}
) {
    val iconRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    @StringRes
    val textRes by derivedStateOf {
        if (expanded) R.string.read_less else R.string.read_more
    }

    TextButton(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        onClick = onClick
    ) {
        Text(
            text = stringResource(textRes),
            style = TextStyle(fontSize = 12.sp)
        )
        Icon(
            modifier = Modifier.rotate(iconRotation),
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = null
        )
    }
}