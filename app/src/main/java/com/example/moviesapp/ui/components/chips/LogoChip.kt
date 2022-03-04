package com.example.moviesapp.ui.components.chips

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.grayScale
import com.example.moviesapp.ui.components.others.TmdbImage
import com.example.moviesapp.ui.theme.White500
import com.example.moviesapp.ui.theme.spacing

@Composable
fun LogoChip(
    modifier: Modifier = Modifier,
    logoPath: String? = null,
    text: String,
    selected: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colors.primary.copy(0.5f)
        } else {
            Color.Gray.copy(0.5f)
        }
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) {
            Color.White
        } else {
            White500
        }
    )

    Column(
        modifier = modifier
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .width(80.dp)
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = borderColor,
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
                contentScale = ContentScale.Fit,
                colorFilter = if (selected) null else ColorFilter.grayScale()
            ) {
                crossfade(true)
            }
        } else {
            Image(
                painter = painterResource(R.drawable.ic_outline_no_photography_24),
                contentDescription = null,
                colorFilter = if (selected) {
                    ColorFilter.tint(MaterialTheme.colors.primary.copy(0.3f))
                } else ColorFilter.grayScale()
            )
        }

        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}