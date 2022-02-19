package com.example.moviesapp.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.moviesapp.R
import com.example.moviesapp.model.FavouriteType
import com.example.moviesapp.ui.theme.White300
import com.example.moviesapp.ui.theme.spacing

@Composable
fun FavouriteEmptyState(
    modifier: Modifier,
    type: FavouriteType,
    onButtonClick: () -> Unit = {}
) {
    @StringRes
    val infoTextResId = when (type) {
        FavouriteType.Movie -> R.string.favourite_empty_movies_info_text
        FavouriteType.TvSeries -> R.string.favourite_empty_tv_series_info_text
    }

    @StringRes
    val buttonLabelResId = when (type) {
        FavouriteType.Movie -> R.string.favourite_empty_navigate_to_movies_button_label
        FavouriteType.TvSeries -> R.string.favourite_empty_navigate_to_tv_series_button_label
    }

    @DrawableRes
    val icon = when (type) {
        FavouriteType.Movie -> R.drawable.ic_outline_movie_24
        FavouriteType.TvSeries -> R.drawable.ic_outline_tv_24
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(White300)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = stringResource(infoTextResId),
            color = White300
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedButton(onClick = onButtonClick) {
            Text(text = stringResource(buttonLabelResId))
        }
    }
}