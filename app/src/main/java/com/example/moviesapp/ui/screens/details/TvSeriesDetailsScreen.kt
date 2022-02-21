package com.example.moviesapp.ui.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.model.TvSeriesRelationInfo
import com.example.moviesapp.other.ifNotNullAndEmpty
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.other.openExternalId
import com.example.moviesapp.other.openVideo
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.screens.details.components.TvSeriesDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.TvSeriesDetailsTopContent
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
    val context = LocalContext.current
    val density = LocalDensity.current

    val tvSeriesDetails by viewModel.tvSeriesDetails.collectAsState()
    val isFavourite by viewModel.isFavourite.collectAsState()

    val similar = viewModel.similarTvSeries.collectAsLazyPagingItems()
    val recommendations = viewModel.tvSeriesRecommendations.collectAsLazyPagingItems()
    val backdrops by viewModel.backdrops.collectAsState()
    val videos by viewModel.videos.collectAsState()
    val nextEpisodeDaysRemaining by viewModel.nextEpisodeDaysRemaining.collectAsState()
    val watchProviders by viewModel.watchProviders.collectAsState()
    val externalIds by viewModel.externalIds.collectAsState()
    val hasReviews by viewModel.hasReviews.collectAsState()

    val scrollState = rememberScrollState()

    var showErrorDialog by remember { mutableStateOf(false) }
    val error: String? by viewModel.error.collectAsState()

    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            PresentableDetailsTopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        topSectionHeight = coordinates.size.height.toFloat()
                    },
                presentable = tvSeriesDetails,
                backdrops = backdrops,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue
            ) {
                TvSeriesDetailsTopContent(
                    modifier = Modifier.fillMaxWidth(),
                    tvSeriesDetails = tvSeriesDetails
                )

                Spacer(modifier = Modifier.weight(1f))

                externalIds?.let { ids ->
                    ExternalIdsSection(
                        modifier = Modifier.fillMaxWidth(),
                        externalIds = ids
                    ) { externalId ->
                        openExternalId(
                            context = context,
                            externalId = externalId
                        )
                    }
                }
            }

            TvSeriesDetailsInfoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .animateContentSize(),
                tvSeriesDetails = tvSeriesDetails,
                nextEpisodeDaysRemaining = nextEpisodeDaysRemaining
            )

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = watchProviders
            ) { providers ->
                if (providers != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        WatchProvidersSection(
                            modifier = Modifier.fillMaxWidth(),
                            watchProviders = providers,
                            title = stringResource(R.string.available_at)
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = tvSeriesDetails?.creators
            ) { creators ->
                creators.ifNotNullAndEmpty { members ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        MemberSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.tv_series_details_creators),
                            members = members,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                        ) { creatorId ->
                            navigator.navigate(PersonDetailsScreenDestination(creatorId))
                        }
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .animateContentSize(),
                targetState = tvSeriesDetails?.seasons
            ) { seasons ->
                seasons.ifNotNullAndEmpty { value ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        SeasonsSection(
                            title = stringResource(R.string.tv_series_details_seasons),
                            seasons = value
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
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = similar
            ) { similar ->
                if (similar.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        PresentableSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.tv_series_details_similar),
                            state = similar,
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
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = recommendations
            ) { recommendations ->
                if (recommendations.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        PresentableSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.tv_series_details_recommendations),
                            state = recommendations,
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
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = videos
            ) { videos ->
                videos.ifNotNullAndEmpty { value ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        VideosSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.tv_series_details_videos),
                            videos = value,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                        ) { video ->
                            openVideo(
                                context = context,
                                video = video
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = hasReviews
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    SectionDivider(modifier = Modifier.fillMaxWidth())
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
            scrollState = scrollState,
            transparentScrollValueLimit = topSectionScrollLimitValue,
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