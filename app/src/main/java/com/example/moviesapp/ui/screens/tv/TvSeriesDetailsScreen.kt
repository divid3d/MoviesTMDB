package com.example.moviesapp.ui.screens.tv

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.LikeButton
import com.example.moviesapp.ui.screens.movies.components.OverviewSection
import com.example.moviesapp.ui.screens.tv.components.TvSeriesDetailsTopSection
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TvSeriesDetailsScreen(
    navigator: DestinationsNavigator,
    tvSeriesId: Int
) {
    val viewModel: TvSeriesDetailsViewModel = hiltViewModel()

    val insets = LocalWindowInsets.current
    val density = LocalDensity.current

    val tvSeriesDetails by viewModel.tvSeriesDetails.collectAsState()

    val scrollState = rememberScrollState()

    var topSectionHeight: Int? by remember(tvSeriesDetails) {
        mutableStateOf(null)
    }

    val statusBarHeight by remember {
        mutableStateOf(
            insets.systemBars.layoutInsets.top
        )
    }

    val appbarHeight by derivedStateOf {
        density.run {
            56.dp.roundToPx()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            TvSeriesDetailsTopSection(
                modifier = Modifier
                    .fillMaxWidth(),
                tvSeriesDetails = tvSeriesDetails
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            tvSeriesDetails?.let { details ->
                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Text(
                        text = details.name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    details.tagline.let { tagline ->
                        if (tagline.isNotEmpty()) {
                            Text(
                                text = "\"$tagline\"",
                                style = TextStyle(fontStyle = FontStyle.Italic)
                            )
                        }
                    }
                    OverviewSection(
                        overview = details.overview
                    )
                }
            }

//            similarMoviesState?.let { lazyPagingItems ->
//                PresentableSection(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    title = "Podobne",
//                    state = lazyPagingItems
//                ) { movieId ->
//                    navigator.navigate(
//                        MovieDetailsScreenDestination(movieId)
//                    )
//                }
//            }
//            moviesRecommendationState?.let { lazyPagingItems ->
//                PresentableSection(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    title = "Polecane",
//                    state = lazyPagingItems
//                ) { movieId ->
//                    navigator.navigate(
//                        MovieDetailsScreenDestination(movieId)
//                    )
//                }
//            }
            Spacer(
                modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large)
            )
        }
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = "Szczegóły serialu",
            backgroundColor = Black500,
            action = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailing = {
                val isFavourite = tvSeriesDetails?.isFavourite == true

                AnimatedVisibility(
                    visible = tvSeriesDetails != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LikeButton(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.small),
                        isFavourite = isFavourite,
                        onClick = {
                            tvSeriesDetails?.let { details ->
                                if (isFavourite) {
                                    viewModel.onUnlikeClick(details)
                                } else {
                                    viewModel.onLikeClick(details)
                                }
                            }
                        }
                    )
                }
            }
        )
    }

}