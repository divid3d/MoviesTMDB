package com.example.moviesapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.model.Episode
import com.example.moviesapp.model.Image
import com.example.moviesapp.other.formatted
import com.example.moviesapp.ui.theme.spacing

@Composable
fun EpisodeChip(
    modifier: Modifier = Modifier,
    episode: Episode,
    expanded: Boolean = false,
    stills: List<Image> = emptyList(),
    onClick: () -> Unit = {}
) {
    val iconRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary.copy(0.5f)),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = episode.name,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )

                    episode.airDate?.let { date ->
                        Text(
                            text = date.formatted(),
                            style = TextStyle(fontWeight = FontWeight.Thin)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Icon(
                    modifier = Modifier.rotate(iconRotation),
                    imageVector = Icons.Filled.ArrowDropDown,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = if (expanded) "collapse" else "expand"
                )
            }

            AnimatedVisibility(
                enter = fadeIn(),
                exit = fadeOut(),
                visible = expanded
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    Text(text = episode.overview, style = TextStyle(fontSize = 12.sp))

                    if (stills.isNotEmpty()) {
                        StillBrowser(
                            modifier = Modifier.fillMaxWidth(),
                            stillPaths = stills
                        )
                    }
                }
            }
        }
    }


}