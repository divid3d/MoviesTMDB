package com.example.moviesapp.ui.screens.movies.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.moviesapp.R
import com.example.moviesapp.model.CastMember
import com.example.moviesapp.ui.theme.spacing

@Composable
fun CastSection(
    modifier: Modifier = Modifier,
    cast: List<CastMember>,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.default)
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        contentPadding = contentPadding
    ) {
        items(cast, key = { member -> member.id to member.character }) { member ->
            CastChip(
                modifier = Modifier.width(80.dp),
                member = member
            )
        }
    }
}

@Composable
fun CastChip(
    modifier: Modifier = Modifier,
    member: CastMember
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (member.profileUrl != null) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                painter = rememberImagePainter(
                    data = member.profileUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                        crossfade(true)
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(width = 1.dp, color = Color.White.copy(0.5f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_outline_no_photography_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White.copy(0.5f))
                )
            }
        }

        Text(
            text = member.character,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = member.name,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}