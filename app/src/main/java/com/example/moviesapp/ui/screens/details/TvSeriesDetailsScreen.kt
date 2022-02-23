package com.example.moviesapp.ui.screens.details

import android.os.Parcelable
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
import com.example.moviesapp.model.*
import com.example.moviesapp.other.*
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.screens.details.components.TvSeriesDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.TvSeriesDetailsTopContent
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvSeriesDetailsScreenArgs(
    val tvSeriesId: Int,
    val startRoute: String
) : Parcelable

@Destination(navArgsDelegate = TvSeriesDetailsScreenArgs::class)
@Composable
fun TvSeriesDetailsScreen(
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

    val similar = uiState.associatedTvSeries.similar.collectAsLazyPagingItems()
    val recommendations = uiState.associatedTvSeries.recommendations.collectAsLazyPagingItems()

    val scrollState = rememberScrollState()

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
                presentable = uiState.tvSeriesDetails,
                backdrops = uiState.associatedContent.backdrops,
                scrollState = scrollState,
                scrollValueLimit = topSectionScrollLimitValue
            ) {
                TvSeriesDetailsTopContent(
                    modifier = Modifier.fillMaxWidth(),
                    tvSeriesDetails = uiState.tvSeriesDetails
                )

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

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = uiState.additionalTvSeriesDetailsInfo.watchProviders
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
                targetState = uiState.tvSeriesDetails?.creators
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
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                            onMemberClick = onCreatorClicked
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .animateContentSize(),
                targetState = uiState.tvSeriesDetails?.seasons
            ) { seasons ->
                seasons.ifNotNullAndEmpty { value ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        SeasonsSection(
                            title = stringResource(R.string.tv_series_details_seasons),
                            seasons = value,
                            onSeasonClick = onSeasonClicked
                        )
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
                            onMoreClick = onSimilarMoreClicked,
                            onPresentableClick = onTvSeriesClicked
                        )
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
                            onMoreClick = onRecommendationsMoreClicked,
                            onPresentableClick = onTvSeriesClicked
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = uiState.associatedContent.videos
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
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                            onVideoClicked = onVideoClicked
                        )
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = uiState.additionalTvSeriesDetailsInfo.hasReviews
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    SectionDivider(modifier = Modifier.fillMaxWidth())
                    ReviewSection(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onReviewsClicked
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