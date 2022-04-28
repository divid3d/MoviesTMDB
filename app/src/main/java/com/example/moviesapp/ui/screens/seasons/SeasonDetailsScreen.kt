package com.example.moviesapp.ui.screens.seasons

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.moviesapp.R
import com.example.moviesapp.other.formatted
import com.example.moviesapp.other.ifNotNullAndEmpty
import com.example.moviesapp.other.openVideo
import com.example.moviesapp.ui.components.chips.EpisodeChip
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.components.sections.MemberSection
import com.example.moviesapp.ui.components.sections.PresentableDetailsTopSection
import com.example.moviesapp.ui.components.sections.VideosSection
import com.example.moviesapp.ui.components.texts.ExpandableText
import com.example.moviesapp.ui.components.texts.LabeledText
import com.example.moviesapp.ui.components.texts.SectionLabel
import com.example.moviesapp.ui.screens.destinations.FavouritesScreenDestination
import com.example.moviesapp.ui.screens.destinations.PersonDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalAnimationApi::class)
object SeasonDetailsScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            TvScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            TvScreenDestination.route,
            FavouritesScreenDestination.route,
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }
}

@Parcelize
data class SeasonDetailsScreenArgs(
    val tvSeriesId: Int,
    val seasonNumber: Int,
    val startRoute: String
) : Parcelable

@Destination(
    navArgsDelegate = SeasonDetailsScreenArgs::class,
    style = SeasonDetailsScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.SeasonDetailsScreen(
    viewModel: SeasonDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onCloseClicked: () -> Unit = {
        navigator.popBackStack(uiState.startRoute, inclusive = false)
    }
    val onMemberClicked = { personId: Int ->
        val destination = PersonDetailsScreenDestination(
            personId = personId,
            startRoute = uiState.startRoute
        )

        navigator.navigate(destination)
    }
    val onEpisodeExpanded: (episodeNumber: Int) -> Unit = viewModel::getEpisodeStills

    SeasonDetailsContent(
        uiState = uiState,
        onCloseClicked = onCloseClicked,
        onBackClicked = onBackClicked,
        onMemberClicked = onMemberClicked,
        onEpisodeExpanded = onEpisodeExpanded
    )
}

@Composable
fun SeasonDetailsContent(
    uiState: SeasonDetailsScreenUiState,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onMemberClicked: (Int) -> Unit,
    onEpisodeExpanded: (episodeNumber: Int) -> Unit
) {
    val context = LocalContext.current

    val lazyState = rememberLazyListState()

    var showErrorDialog by remember { mutableStateOf(false) }

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
        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                PresentableDetailsTopSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    presentable = uiState.seasonDetails
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        uiState.seasonDetails?.seasonNumber?.let { number ->
                            LabeledText(
                                label = stringResource(R.string.season_details_season_number_label),
                                text = number.toString()
                            )
                        }
                        uiState.episodeCount?.let { count ->
                            LabeledText(
                                label = stringResource(R.string.season_details_episodes_count_label),
                                text = count.toString()
                            )
                        }
                        uiState.seasonDetails?.airDate?.let { date ->
                            LabeledText(
                                label = stringResource(R.string.season_details_air_date_label),
                                text = date.formatted()
                            )
                        }
                    }
                }
            }

            item {
                Crossfade(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium)
                        .animateContentSize(),
                    targetState = uiState.seasonDetails
                ) { details ->
                    if (details != null) {
                        Column(
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                            Text(
                                text = details.name,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (details.overview.isNotBlank()) {
                                ExpandableText(
                                    modifier = Modifier.fillMaxSize(),
                                    text = details.overview
                                )
                            }
                        }
                    }
                }
            }

            item {
                Crossfade(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium)
                        .animateContentSize(),
                    targetState = uiState.aggregatedCredits?.cast
                ) { cast ->
                    cast.ifNotNullAndEmpty { members ->
                        MemberSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.season_details_cast_label),
                            members = members,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                            onMemberClick = onMemberClicked
                        )
                    }
                }
            }

            item {
                Crossfade(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium)
                        .animateContentSize(),
                    targetState = uiState.aggregatedCredits?.crew
                ) { cast ->
                    cast.ifNotNullAndEmpty { members ->
                        MemberSection(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.season_details_crew_label),
                            members = members,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
                            onMemberClick = onMemberClicked
                        )
                    }
                }
            }

            item {
                Crossfade(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium)
                        .animateContentSize(),
                    targetState = uiState.videos
                ) { videos ->
                    videos.ifNotNullAndEmpty { value ->
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

            uiState.seasonDetails?.episodes?.let { episodes ->
                if (episodes.isNotEmpty()) {
                    item {
                        SectionLabel(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.spacing.medium)
                                .padding(
                                    top = MaterialTheme.spacing.medium,
                                    bottom = MaterialTheme.spacing.small
                                ),
                            text = stringResource(R.string.season_details_episodes_label)
                        )
                    }
                }

                itemsIndexed(episodes) { index, episode ->
                    val bottomPadding = if (index < episodes.count() - 1) {
                        MaterialTheme.spacing.medium
                    } else {
                        MaterialTheme.spacing.default
                    }

                    var expanded by rememberSaveable(episode.id.toString()) {
                        mutableStateOf(false)
                    }

                    val stills by derivedStateOf {
                        uiState.episodeStills.getOrElse(
                            episode.episodeNumber,
                            defaultValue = { null })
                    }

                    EpisodeChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(bottom = bottomPadding),
                        episode = episode,
                        stills = stills,
                        expanded = expanded
                    ) {
                        expanded = !expanded
                        if (expanded) {
                            onEpisodeExpanded(episode.episodeNumber)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large))
            }
        }

        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.season_details_appbar_label),
            backgroundColor = Color.Black.copy(0.7f),
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