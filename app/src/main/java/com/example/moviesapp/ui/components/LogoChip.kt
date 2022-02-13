package com.example.moviesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.ui.theme.spacing

@Composable
fun LogoChip(
    modifier: Modifier = Modifier,
    logoPath: String? = null,
    text: String
) {
    Column(
        modifier = modifier
            .width(80.dp)
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary.copy(0.5f),
                shape = MaterialTheme.shapes.small
            )
            .padding(MaterialTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        if (logoPath != null) {
            TmdbImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                imagePath = logoPath,
                imageType = ImageUrlParser.ImageType.Logo,
                contentScale = ContentScale.Fit
            ) {
                crossfade(true)
            }
        } else {
            Image(
                painter = painterResource(R.drawable.ic_outline_no_photography_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary.copy(0.3f))
            )
        }

        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}