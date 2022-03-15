package com.example.moviesapp.ui.screens.details.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.size.OriginalSize
import coil.size.Scale
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.ui.components.items.NoPhotoPresentableItem
import com.example.moviesapp.ui.components.others.TmdbImage
import com.example.moviesapp.ui.theme.Size
import com.example.moviesapp.ui.theme.sizes

@Composable
fun PersonProfileImage(
    profilePath: String?,
    modifier: Modifier = Modifier,
    size: Size = MaterialTheme.sizes.presentableItemBig
) {
    Card(
        modifier = modifier
            .width(size.width)
            .height(size.height),
        shape = MaterialTheme.shapes.medium
    ) {
        if (profilePath != null) {
            TmdbImage(
                modifier = Modifier.fillMaxSize(),
                imagePath = profilePath,
                imageType = ImageUrlParser.ImageType.Profile
            ) {
                size(OriginalSize)
                scale(Scale.FILL)
                crossfade(true)
            }
        } else {
            NoPhotoPresentableItem(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}