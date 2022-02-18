package com.example.moviesapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.model.Video
import com.example.moviesapp.model.VideoSite
import com.example.moviesapp.model.getThumbnailUrl
import com.example.moviesapp.ui.theme.sizes
import com.example.moviesapp.ui.theme.spacing

@Composable
fun VideosSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    videos: List<Video>,
    contentPadding: PaddingValues,
    onVideoClicked: (Video) -> Unit = { }
) {
    Column(modifier = modifier) {
        title?.let { title ->
            SectionLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                text = title
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            contentPadding = contentPadding
        ) {
            items(videos) { video ->
                VideoItem(
                    video = video,
                    onVideoClick = {
                        onVideoClicked(video)
                    }
                )
            }
        }
    }
}

@Composable
private fun VideoItem(
    modifier: Modifier = Modifier,
    video: Video,
    onVideoClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(MaterialTheme.sizes.videoItem.width)
            .height(MaterialTheme.sizes.videoItem.height),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(0.5f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onVideoClick() },
                painter = rememberImagePainter(
                    data = video.getThumbnailUrl(),
                    builder = {
                        fadeIn()
                    }
                ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )

            Icon(
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colors.primary.copy(0.7f)
            )

            SiteIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = MaterialTheme.spacing.extraSmall,
                        end = MaterialTheme.spacing.extraSmall
                    ),
                site = video.site
            )
        }
    }

}

@Composable
private fun SiteIcon(modifier: Modifier = Modifier, site: VideoSite) {
    @DrawableRes
    val drawableRes = when (site) {
        VideoSite.YouTube -> R.drawable.ic_youtube
        VideoSite.Vimeo -> R.drawable.ic_vimeo
    }

    Image(
        modifier = modifier,
        painter = painterResource(drawableRes),
        contentDescription = null
    )
}