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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.movies.components.GenresSection
import com.example.moviesapp.ui.screens.movies.components.OverviewSection
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
    val similar = viewModel.similarTvSeries?.collectAsLazyPagingItems()
    val recommendations = viewModel.tvSeriesRecommendations?.collectAsLazyPagingItems()
    val seasonNumbers by viewModel.seasonNumbers.collectAsState()

    val selectedSeasonNumber by viewModel.selectedSeasonNumber.collectAsState()
    val episodes by viewModel.episodes.collectAsState()

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
            PresentableDetailsTopSection(
                modifier = Modifier
                    .fillMaxWidth(),
                presentable = tvSeriesDetails
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                    tvSeriesDetails?.let { details ->
                        LabeledText(
                            label = stringResource(R.string.tv_series_details_type),
                            text = details.type
                        )
                        LabeledText(
                            label = stringResource(R.string.tv_series_details_status),
                            text = details.status
                        )
                        LabeledText(
                            label = stringResource(R.string.tv_series_details_in_production),
                            text = stringResource(if (details.inProduction) R.string.yes else R.string.no)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                            Label(label = stringResource(R.string.tv_series_details_genres))
                            GenresSection(genres = details.genres)
                        }
                    }
                }
            }
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
            SeasonsList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                seasonNumbers = seasonNumbers,
                selectedSeason = selectedSeasonNumber
            ) { seasonNumber ->
                viewModel.getTvSeason(seasonNumber)
            }
            EpisodesList(
                modifier = Modifier.fillMaxWidth(),
                episodes = episodes
            )
            similar?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = stringResource(R.string.tv_series_details_similar),
                    showMoreButton = false,
                    state = lazyPagingItems
                ) { tvSeriesId ->
                    navigator.navigate(
                        TvSeriesDetailsScreenDestination(tvSeriesId)
                    )
                }
            }
            recommendations?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = stringResource(R.string.tv_series_details_recommendations),
                    showMoreButton = false,
                    state = lazyPagingItems
                ) { tvSeriesId ->
                    navigator.navigate(
                        TvSeriesDetailsScreenDestination(tvSeriesId)
                    )
                }
            }
            Spacer(
                modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large)
            )
        }
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.tv_series_details_label),
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