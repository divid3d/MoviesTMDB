package com.example.moviesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.transform.CircleCropTransformation
import com.example.moviesapp.R
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.ui.theme.spacing

@Composable
fun MemberResultChip(
    modifier: Modifier = Modifier,
    profilePath: String?,
    firstLine: String?,
    secondLine: String?,
    onClick: () -> Unit = {}
) {
    var secondLineExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profilePath != null) {
            TmdbImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = CircleShape
                    )
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .clickable { onClick() },
                imagePath = profilePath,
                imageType = ImageUrlParser.ImageType.Profile
            ) {
                transformations(CircleCropTransformation())
                crossfade(true)
            }
        } else {
            MemberNoPhotoChip(onClick = { onClick() })
        }

        firstLine?.let { firstLine ->
            if (firstLine.isNotBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = firstLine,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        secondLine?.let { secondLine ->
            if (secondLine.isNotBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            secondLineExpanded = !secondLineExpanded
                        },
                    text = secondLine,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = if (secondLineExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun MemberNoPhotoChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = MaterialTheme.colors.surface, shape = CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_outline_no_photography_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colors.primary.copy(alpha = 0.3f)
            )
        )
    }
}