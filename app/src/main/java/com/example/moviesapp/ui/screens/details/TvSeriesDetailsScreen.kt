package com.example.moviesapp.ui.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.model.RelationType
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.model.TvSeriesRelationInfo
import com.example.moviesapp.other.openExternalId
import com.example.moviesapp.other.openVideo
import com.example.moviesapp.other.yearRangeString
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.*
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

    val similar = viewModel.similarTvSeries?.collectAsLazyPagingItems()
    val recommendations = viewModel.tvSeriesRecommendations?.collectAsLazyPagingItems()
    val backdrops by viewModel.backdrops.collectAsState()
    val videos by viewModel.videos.collectAsState()
    val nextEpisodeDaysRemaining by viewModel.nextEpisodeDaysRemaining.collectAsState()
    val watchProviders by viewModel.watchProviders.collectAsState()
    val externalIds by viewModel.externalIds.collectAsState()
    val hasReviews by viewModel.hasReviews.collectAsState()

    val otherOriginalTitle: Boolean by derivedStateOf {
        tvSeriesDetails?.run { !originalName.isNullOrEmpty() && title != originalName } ?: false
    }

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
                .verticalScroll(scrollState)
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
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                    tvSeriesDetails?.let { details ->
                        LabeledText(
                            label = stringResource(R.string.tv_series_details_type),
                            text = stringResource(details.type.getLabel())
                        )

                        LabeledText(
                            label = stringResource(R.string.tv_series_details_status),
                            text = stringResource(details.status.getLabel())
                        )

                        LabeledText(
                            label = stringResource(R.string.tv_series_details_in_production),
                            text = stringResource(if (details.inProduction) R.string.yes else R.string.no)
                        )
                    }
                }

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

            tvSeriesDetails?.let { details ->
                Column(
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                            text = details.name,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        details.originalName?.let { name ->
                            if (otherOriginalTitle) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.spacing.medium),
                                    text = name
                                )
                            }
                        }
                        AdditionalInfoText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium),
                            infoTexts = details.run {
                                listOfNotNull(
                                    yearRangeString(
                                        from = firstAirDate,
                                        to = lastAirDate
                                    ),
                                    nextEpisodeDaysRemaining?.let { days ->
                                        when (days) {
                                            0L -> stringResource(R.string.next_episode_today_text)
                                            1L -> stringResource(R.string.next_episode_tomorrow_text)
                                            else -> stringResource(
                                                R.string.next_episode_days_text,
                                                days
                                            )
                                        }
                                    }
                                )
                            }
                        )
                    }

                    if (details.genres.isNotEmpty()) {
                        GenresSection(
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                            genres = details.genres
                        )
                    }

                    Column {
                        details.tagline.let { tagline ->
                            if (tagline.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                                    text = "\"$tagline\"",
                                    style = TextStyle(fontStyle = FontStyle.Italic)
                                )
                            }
                        }

                        ExpandableText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium),
                            text = details.overview
                        )
                    }

                    SectionDivider(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.large)
                    )
                }
            }

            watchProviders?.let { providers ->
                WatchProvidersSection(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.small)
                        .fillMaxWidth()
                        .animateContentSize(),
                    watchProviders = providers,
                    title = stringResource(R.string.available_at)
                )
            }

            tvSeriesDetails?.creators?.let { creators ->
                if (creators.isNotEmpty()) {
                    MemberSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.small)
                            .animateContentSize(),
                        title = stringResource(R.string.tv_series_details_creators),
                        members = creators,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                    ) { creatorId ->
                        navigator.navigate(PersonDetailsScreenDestination(creatorId))
                    }
                }
            }

            videos?.let { videos ->
                if (videos.isNotEmpty()) {
                    VideosSection(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.small)
                            .fillMaxWidth()
                            .animateContentSize(),
                        title = stringResource(R.string.tv_series_details_videos),
                        videos = videos,
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
                    ) { video ->
                        openVideo(
                            context = context,
                            video = video
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
            }

            tvSeriesDetails?.seasons?.let { seasons ->
                if (seasons.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(vertical = MaterialTheme.spacing.small)
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

                    SectionDivider()
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