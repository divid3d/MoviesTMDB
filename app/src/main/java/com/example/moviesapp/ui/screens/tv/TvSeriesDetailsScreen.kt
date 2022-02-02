package com.example.moviesapp.ui.screens.tv

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.model.TvSeriesRelationInfo
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.screens.destinations.RelatedTvSeriesDestination
import com.example.moviesapp.ui.screens.destinations.ReviewsScreenDestination
import com.example.moviesapp.ui.screens.destinations.SeasonDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.movies.components.OverviewSection
import com.example.moviesapp.ui.screens.reviews.ReviewsScreenNavArgs
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TvSeriesDetailsScreen(
    viewModel: TvSeriesDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    tvSeriesId: Int,
    startRoute: String = TvScreenDestination.route
) {
    val tvSeriesDetails by viewModel.tvSeriesDetails.collectAsState()
    val isFavourite by viewModel.isFavourite.collectAsState()

    val similar = viewModel.similarTvSeries?.collectAsLazyPagingItems()
    val recommendations = viewModel.tvSeriesRecommendations?.collectAsLazyPagingItems()
    val backdrops by viewModel.backdrops.collectAsState()
    val hasReviews by viewModel.hasReviews.collectAsState()


    val scrollState = rememberScrollState()

    var showErrorDialog by remember { mutableStateOf(false) }
    val error: String? by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        showErrorDialog = error != null
    }

    BackHandler(showErrorDialog) {
        showErrorDialog = false
    }

    if (showErrorDialog) {
        ErrorDialog(
            onDismissRequest = {
                showErrorDialog = false
            },
            onConfirmClick = {
                showErrorDialog = false
            }
        )
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
                modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
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
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                                text = "\"$tagline\"",
                                style = TextStyle(fontStyle = FontStyle.Italic)
                            )
                        }
                    }

                    OverviewSection(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                        overview = details.overview
                    )

                    SectionDivider(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.large)
                    )
                }
            }

            tvSeriesDetails?.networks?.let { networks ->
                if (networks.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.medium)
                            .animateContentSize()
                    ) {
                        SectionLabel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium),
                            text = stringResource(R.string.tv_series_details_networks)
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                        NetworksList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium),
                            networks = networks
                        )

                        SectionDivider(
                            modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
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
                        modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
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
                    state = lazyPagingItems,
                    onMoreClick = {
                        val tvSeriesRelationInfo = TvSeriesRelationInfo(
                            tvSeriesId = tvSeriesId,
                            type = RelationType.Similar
                        )

                        navigator.navigate(
                            RelatedTvSeriesDestination(tvSeriesRelationInfo)
                        )
                    }
                ) { tvSeriesId ->
                    navigator.navigate(
                        TvSeriesDetailsScreenDestination(
                            tvSeriesId = tvSeriesId,
                            startRoute = startRoute
                        )
                    )
                }

                SectionDivider(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                )
            }

            recommendations?.let { lazyPagingItems ->
                PresentableSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                        .animateContentSize(),
                    title = stringResource(R.string.tv_series_details_recommendations),
                    state = lazyPagingItems,
                    onMoreClick = {
                        val tvSeriesRelationInfo = TvSeriesRelationInfo(
                            tvSeriesId = tvSeriesId,
                            type = RelationType.Recommended
                        )

                        navigator.navigate(
                            RelatedTvSeriesDestination(tvSeriesRelationInfo)
                        )
                    }
                ) { tvSeriesId ->
                    navigator.navigate(
                        TvSeriesDetailsScreenDestination(
                            tvSeriesId = tvSeriesId,
                            startRoute = startRoute
                        )
                    )
                }
            }

            if (hasReviews) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionDivider(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                    )

                    ReviewSection(modifier = Modifier.fillMaxWidth()) {
                        val args = ReviewsScreenNavArgs(
                            mediaId = tvSeriesId,
                            type = MediaType.Tv
                        )

                        navigator.navigate(
                            ReviewsScreenDestination(args)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large)
            )
        }
        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.tv_series_details_label),
            backgroundColor = Color.Black.copy(0.7f),
            action = {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "go back",
                        tint = MaterialTheme.colors.primary
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
                            contentDescription = "close",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )
    }

}