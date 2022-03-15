package com.example.moviesapp.ui.components.others

import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.ImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.moviesapp.LocalImageUrlParser
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.getMaxSizeInt


@Composable
inline fun rememberTmdbImagePainter(
    path: String?,
    type: ImageUrlParser.ImageType,
    preferredSize: Size,
    strategy: ImageUrlParser.MatchingStrategy = ImageUrlParser.MatchingStrategy.FirstBiggerWidth,
    onExecute: ImagePainter.ExecuteCallback = ImagePainter.ExecuteCallback.Default,
    builder: ImageRequest.Builder.() -> Unit = {},
): ImagePainter {
    val imageUrlParser = LocalImageUrlParser.current

    val imageUrl = imageUrlParser?.getImageUrl(
        path = path,
        type = type,
        preferredSize = preferredSize,
        strategy = strategy
    )

    return rememberImagePainter(imageUrl, LocalImageLoader.current, onExecute, builder)
}

@Composable
fun TmdbImage(
    imagePath: String?,
    imageType: ImageUrlParser.ImageType,
    modifier: Modifier = Modifier,
    strategy: ImageUrlParser.MatchingStrategy = ImageUrlParser.MatchingStrategy.FirstBiggerWidth,
    contentScale: ContentScale = ContentScale.FillBounds,
    colorFilter: ColorFilter? = null,
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        val (maxWith, maxHeight) = getMaxSizeInt()

        val painter = rememberTmdbImagePainter(
            path = imagePath,
            type = imageType,
            preferredSize = Size(maxWith, maxHeight),
            strategy = strategy
        ) {
            builder()
        }

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = null,
            colorFilter = colorFilter,
            contentScale = contentScale
        )
    }
}