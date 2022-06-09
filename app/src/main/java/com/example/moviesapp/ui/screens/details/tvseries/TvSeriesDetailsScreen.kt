package com.example.moviesapp.ui.screens.details.tvseries

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
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
import com.example.moviesapp.model.*
import com.example.moviesapp.other.isNotEmpty
import com.example.moviesapp.other.openExternalId
import com.example.moviesapp.other.openVideo
import com.example.moviesapp.other.shareImdb
import com.example.moviesapp.ui.components.buttons.LikeButton
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.components.others.AnimatedContentContainer
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.components.sections.*
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.screens.details.components.TvSeriesDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.TvSeriesDetailsTopContent
import com.example.moviesapp.ui.theme.Black300
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(
    navArgsDelegate = TvSeriesDetailsScreenArgs::class,
    style = TvSeriesDetailsScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.TvSeriesDetailsScreen(
    viewModel: TvSeriesDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onFavouriteClicked: (details: TvSeriesDetails) -> Unit = { details ->
        if (uiState.additionalTvSeriesDetailsInfo.isFavourite) {
            viewModel.onUnlikeClick(details)
        } else {
            viewModel.onLikeClick(details)
        }
    }
    val onCloseClicked: () -> Unit = {
        navigator.popBackStack(uiState.startRoute, inclusive = false)
    }
    val onExternalIdClicked = { id: ExternalId ->
        openExternalId(
            context = context,
            externalId = id
        )
    }
    val onShareClicked = { details: ShareDetails ->
        shareImdb(
            context = context,
            details = details
        )
    }
    val onVideoClicked = { video: Video ->
        openVideo(
            context = context,
            video = video
        )
    }
    val onCreatorClicked = { personId: Int ->
        val destination = PersonDetailsScreenDestination(
            personId = personId,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }
    val onTvSeriesClicked = { tvSeriesId: Int ->
        val destination = TvSeriesDetailsScreenDestination(
            tvSeriesId = tvSeriesId,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }
    val onReviewsClicked: () -> Unit = {
        val tvSeriesId = uiState.tvSeriesDetails?.id

        if (tvSeriesId != null) {
            val destination = ReviewsScreenDestination(
                startRoute = uiState.startRoute,
                mediaId = tvSeriesId,
                type = MediaType.Movie
            )

            navigator.navigate(destination)
        }
    }
    val onSeasonClicked = { seasonNumber: Int ->
        val tvSeriesId = uiState.tvSeriesDetails?.id

        if (tvSeriesId != null) {
            val destination = SeasonDetailsScreenDestination(
                tvSeriesId = tvSeriesId,
                seasonNumber = seasonNumber,
                startRoute = uiState.startRoute
            )

            navigator.navigate(destination)
        }
    }
    val onSimilarMoreClicked = {
        val tvSeriesId = uiState.tvSeriesDetails?.id

        if (tvSeriesId != null) {
            val destination = RelatedTvSeriesScreenDestination(
                tvSeriesId = tvSeriesId,
                type = RelationType.Similar,
                startRoute = uiState.startRoute
            )

            navigator.navigate(destination)
        }
    }

    val onRecommendationsMoreClicked = {
        val tvSeriesId = uiState.tvSeriesDetails?.id

        if (tvSeriesId != null) {
            val destination = RelatedTvSeriesScreenDestination(
                tvSeriesId = tvSeriesId,
                type = RelationType.Recommended,
                startRoute = uiState.startRoute
            )

            navigator.navigate(destination)
        }
    }

    TvSeriesDetailsScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onExternalIdClicked = onExternalIdClicked,
        onShareClicked = onShareClicked,
        onVideoClicked = onVideoClicked,
        onFavouriteClicked = onFavouriteClicked,
        onCloseClicked = onCloseClicked,
        onCreatorClicked = onCreatorClicked,
        onTvSeriesClicked = onTvSeriesClicked,
        onSeasonClicked = onSeasonClicked,
        onSimilarMoreClicked = onSimilarMoreClicked,
        onRecommendationsMoreClicked = onRecommendationsMoreClicked,
        onReviewsClicked = onReviewsClicked
    )
}

@Composable
fun TvSeriesDetailsScreenContent(
    uiState: TvSeriesDetailsScreenUiState,
    onBackClicked: () -> Unit,
    onExternalIdClicked: (id: ExternalId) -> Unit,
    onShareClicked: (details: ShareDetails) -> Unit,
    onVideoClicked: (video: Video) -> Unit,
    onFavouriteClicked: (details: TvSeriesDetails) -> Unit,
    onCloseClicked: () -> Unit,
    onCreatorClicked: (personId: Int) -> Unit,
    onTvSeriesClicked: (tvSeriesId: Int) -> Unit,
    onSeasonClicked: (seasonNumber: Int) -> Unit,
    onSimilarMoreClicked: () -> Unit,
    onRecommendationsMoreClicked: () -> Unit,
    onReviewsClicked: () -> Unit
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val similar = uiState.associatedTvSeries.similar.collectAsLazyPagingItems()
    val recommendations = uiState.associatedTvSeries.recommendations.collectAsLazyPagingItems()

    val scrollState = rememberScrollState()
    val scrollToStart = {
        coroutineScope.launch {
            scrollState.animateScrollTo(0)
        }
    }

    val imdbExternalId by derivedStateOf {
        uiState.associatedContent.externalIds?.filterIsInstance<ExternalId.Imdb>()?.firstOrNull()
    }

    var showErrorDialog by remember { mutableStateOf(false) }

    var topSectionHeight: Float? by remember { mutableStateOf(null) }
    val appbarHeight = density.run { 56.dp.toPx() }
    val topSectionScrollLimitValue: Float? = topSectionHeight?.minus(appbarHeight)

    LaunchedEffect(uiState.error) {
        showErrorDialog = uiState.error != null
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
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
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
                presentable = uiState.tvSeriesDetails,
                backdrops = uiState.associatedContent.backdrops,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Black300,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(MaterialTheme.spacing.small)
                ) {
                    TvSeriesDetailsTopContent(
                        modifier = Modifier.fillMaxWidth(),
                        tvSeriesDetails = uiState.tvSeriesDetails
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                uiState.associatedContent.externalIds?.let { ids ->
                    ExternalIdsSection(
                        modifier = Modifier.fillMaxWidth(),
                        externalIds = ids,
                        onExternalIdClick = onExternalIdClicked
                    )
                }
            }

            TvSeriesDetailsInfoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .animateContentSize(),
                tvSeriesDetails = uiState.tvSeriesDetails,
                nextEpisodeDaysRemaining = uiState.additionalTvSeriesDetailsInfo.nextEpisodeRemainingDays,
                imdbExternalId = imdbExternalId,
                onShareClicked = onShareClicked
            )

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = uiState.additionalTvSeriesDetailsInfo.watchProviders != null
            ) {
                if (uiState.additionalTvSeriesDetailsInfo.watchProviders != null) {
                    WatchProvidersSection(
                        modifier = Modifier.fillMaxWidth(),
                        watchProviders = uiState.additionalTvSeriesDetailsInfo.watchProviders,
                        title = stringResource(R.string.available_at)
                    )
                }
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = !uiState.tvSeriesDetails?.creators.isNullOrEmpty()
            ) {
                MemberSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.tv_series_details_creators),
                    members = uiState.tvSeriesDetails?.creators ?: emptyList(),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                    onMemberClick = onCreatorClicked
                )
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = !uiState.tvSeriesDetails?.seasons.isNullOrEmpty()
            ) {
                SeasonsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(vertical = MaterialTheme.spacing.small),
                    title = stringResource(R.string.tv_series_details_seasons),
                    seasons = uiState.tvSeriesDetails?.seasons ?: emptyList(),
                    onSeasonClick = onSeasonClicked
                )
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = recommendations.isNotEmpty()
            ) {
                PresentableSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.tv_series_details_recommendations),
                    state = recommendations,
                    showLoadingAtRefresh = false,
                    onMoreClick = onRecommendationsMoreClicked,
                    onPresentableClick = { tvSeriesId ->
                        if (tvSeriesId != uiState.tvSeriesDetails?.id) {
                            onTvSeriesClicked(tvSeriesId)
                        } else {
                            scrollToStart()
                        }
                    }
                )
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = similar.isNotEmpty()
            ) {
                PresentableSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.tv_series_details_similar),
                    state = similar,
                    showLoadingAtRefresh = false,
                    onMoreClick = onSimilarMoreClicked,
                    onPresentableClick = { tvSeriesId ->
                        if (tvSeriesId != uiState.tvSeriesDetails?.id) {
                            onTvSeriesClicked(tvSeriesId)
                        } else {
                            scrollToStart()
                        }
                    }
                )
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = !uiState.associatedContent.videos.isNullOrEmpty()
            ) {
                VideosSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.tv_series_details_videos),
                    videos = uiState.associatedContent.videos ?: emptyList(),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                    onVideoClicked = onVideoClicked
                )
            }

            AnimatedContentContainer(
                modifier = Modifier.fillMaxWidth(),
                visible = uiState.additionalTvSeriesDetailsInfo.reviewsCount > 0
            ) {
                ReviewSection(
                    modifier = Modifier.fillMaxWidth(),
                    count = uiState.additionalTvSeriesDetailsInfo.reviewsCount,
                    onClick = onReviewsClicked
                )
            }

            Spacer(
                modifier = Modifier.windowInsetsBottomHeight(
                    insets = WindowInsets(bottom = MaterialTheme.spacing.medium)
                )
            )
        }

        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.tv_series_details_label),
            backgroundColor = Color.Black.copy(0.7f),
            scrollState = scrollState,
            transparentScrollValueLimit = topSectionScrollLimitValue,
            action = {
                IconButton(onClick = onBackClicked) {
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
                        isFavourite = uiState.additionalTvSeriesDetailsInfo.isFavourite,
                        onClick = {
                            val details = uiState.tvSeriesDetails

                            if (details != null) {
                                onFavouriteClicked(details)
                            }
                        }
                    )
                    IconButton(
                        onClick = onCloseClicked
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