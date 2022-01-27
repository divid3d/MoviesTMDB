package com.example.moviesapp.ui.screens.tv

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.screens.destinations.SeasonDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
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
    tvSeriesId: Int,
    startRoute: String = TvScreenDestination.route
) {
    val viewModel: TvSeriesDetailsViewModel = hiltViewModel()
    val density = LocalDensity.current
    val insets = LocalWindowInsets.current

    val tvSeriesDetails by viewModel.tvSeriesDetails.collectAsState()
    val isFavourite by viewModel.isFavourite.collectAsState()

    val similar = viewModel.similarTvSeries?.collectAsLazyPagingItems()
    val recommendations = viewModel.tvSeriesRecommendations?.collectAsLazyPagingItems()
    val backdrops by viewModel.backdrops.collectAsState()


    val scrollState = rememberScrollState()

    var topSectionHeight: Int? by remember { mutableStateOf(null) }
    val appBarHeight by remember { mutableStateOf(density.run { 56.dp.toPx() }) }
    val statusBarHeight: Int by remember {
        mutableStateOf(insets.statusBars.top)
    }

    val appbarColor: Color by derivedStateOf {
        topSectionHeight?.let { height ->
            val alpha =
                (scrollState.value.toFloat() / (height - appBarHeight - statusBarHeight)).coerceIn(
                    0f,
                    1f
                ) * 0.5f + 0.5f

            Color.Black.copy(alpha)
        } ?: Black500
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
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        topSectionHeight = coordinates.size.height
                    },
                presentable = tvSeriesDetails,
                backdrops = backdrops
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

            tvSeriesDetails?.let { details ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .animateContentSize(),
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
            SectionDivider(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.large,
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                )
            )

            tvSeriesDetails?.networks?.let { networks ->
                if (networks.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            )
                            .animateContentSize()
                    ) {
                        SectionLabel(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.tv_series_details_networks)
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        NetworksList(
                            modifier = Modifier.fillMaxWidth(),
                            networks = networks
                        )

                        SectionDivider(
                            modifier = Modifier.padding(
                                top = MaterialTheme.spacing.medium,
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.small,
                            )
                        )
                    }
                }
            }

            tvSeriesDetails?.seasons?.let { seasons ->
                if (seasons.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    ) {
                        SeasonsSection(
                            title = stringResource(R.string.tv_series_details_seasons),
                            seasons = seasons
                        ) { seasonNumber ->
                            tvSeriesDetails?.id?.let { id ->

                                val seasonInfo = SeasonInfo(
                                    tvSeriesId = id,
                                    seasonNumber = seasonNumber
                                )
                                navigator.navigate(
                                    SeasonDetailsScreenDestination(
                                        seasonInfo = seasonInfo,
                                        startRoute = startRoute
                                    )
                                )
                            }
                        }
                    }

                    SectionDivider(
                        modifier = Modifier.padding(
                            top = MaterialTheme.spacing.medium,
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.small,
                        )
                    )
                }
            }

            similar?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.tv_series_details_similar),
                    showMoreButton = false,
                    state = lazyPagingItems
                ) { tvSeriesId ->
                    navigator.navigate(
                        TvSeriesDetailsScreenDestination(
                            tvSeriesId = tvSeriesId,
                            startRoute = startRoute
                        )
                    )
                }

                SectionDivider(
                    modifier = Modifier.padding(
                        top = MaterialTheme.spacing.medium,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.small,
                    )
                )
            }
            recommendations?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.tv_series_details_recommendations),
                    showMoreButton = false,
                    state = lazyPagingItems
                ) { tvSeriesId ->
                    navigator.navigate(
                        TvSeriesDetailsScreenDestination(
                            tvSeriesId = tvSeriesId,
                            startRoute = startRoute
                        )
                    )
                }

                SectionDivider(
                    modifier = Modifier.padding(
                        top = MaterialTheme.spacing.medium,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.small,
                    )
                )
            }
            Spacer(
                modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large)
            )
        }
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.tv_series_details_label),
            backgroundColor = appbarColor,
            action = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            trailing = {
                Row(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                    LikeButton(
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
                    IconButton(
                        onClick = {
                            navigator.popBackStack(startRoute, inclusive = false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }

}