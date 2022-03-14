package com.example.moviesapp.ui.components.others

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.example.moviesapp.R
import com.example.moviesapp.ui.theme.White300
import com.example.moviesapp.ui.theme.spacing

@Composable
fun FilterEmptyState(
    modifier: Modifier = Modifier,
    onFilterButtonClicked: () -> Unit = {}
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_search))
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = PorterDuffColorFilter(
                MaterialTheme.colors.primary.copy(alpha = 0.7f).toArgb(),
                PorterDuff.Mode.SRC_ATOP
            ),
            keyPath = arrayOf(
                "**"
            )
        )
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier.size(160.dp),
            composition = composition,
            speed = 0.2f,
            iterations = LottieConstants.IterateForever,
            dynamicProperties = dynamicProperties
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = stringResource(R.string.filter_empty_info_text),
            color = White300
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        OutlinedButton(onClick = onFilterButtonClicked) {
            Text(text = stringResource(R.string.filter_empty_button_change_filters_label))
        }
    }
}