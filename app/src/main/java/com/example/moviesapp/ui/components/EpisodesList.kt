package com.example.moviesapp.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import com.example.moviesapp.model.Episode
import com.example.moviesapp.ui.theme.spacing

@Composable
fun EpisodesList(
    modifier: Modifier = Modifier,
    episodes: List<Episode>,
    onEpisodeClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(episodes) { episode ->
                Box(
                    modifier = Modifier
                        .width(maxWidth)
                ) {
                    Image(
                        modifier = Modifier.fillParentMaxSize(),
                        painter = rememberImagePainter(
                            data = episode.stillUrl,
                            builder = {
                                fadeIn(animationSpec = spring())
                                transformations(
                                    BlurTransformation(
                                        context = context,
                                        radius = 16f,
                                        sampling = 4f
                                    )
                                )
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                }

            }
        }
    }

}