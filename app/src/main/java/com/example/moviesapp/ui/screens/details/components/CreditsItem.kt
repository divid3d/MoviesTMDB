package com.example.moviesapp.ui.screens.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.size.OriginalSize
import coil.size.Scale
import com.example.moviesapp.R
import com.example.moviesapp.model.CreditsPresentable
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.ui.components.TmdbImage
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.Size
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing

@Composable
fun CreditsItem(
    modifier: Modifier = Modifier,
    creditsPresentable: CreditsPresentable,
    size: Size = MaterialTheme.sizes.presentableItemSmall,
    onClick: () -> Unit = {}
) {
    var infoText by remember(creditsPresentable) {
        mutableStateOf(creditsPresentable.infoText)
    }

    Column(
        modifier = Modifier.width(size.width),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Card(
            modifier = modifier
                .width(size.width)
                .height(size.height)
                .clickable { onClick() },
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (creditsPresentable.posterPath != null) {
                    TmdbImage(
                        modifier = Modifier.fillMaxSize(),
                        imagePath = creditsPresentable.posterPath,
                        imageType = ImageUrlParser.ImageType.Profile
                    ) {
                        size(OriginalSize)
                        scale(Scale.FILL)
                        crossfade(true)
                    }
                } else {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.surface)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_outline_no_photography_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary.copy(alpha = 0.3f))
                        )
                    }
                }

                creditsPresentable.title?.let { title ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Black500)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(MaterialTheme.spacing.extraSmall),
                            text = title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }

        infoText?.let { text ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = TextStyle(
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (result.lineCount < 2) {
                        infoText = infoText?.plus("\n")
                    }
                }
            )
        }
    }


}