package com.example.moviesapp.ui.screens.seasons

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
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
import com.example.moviesapp.ui.components.*
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.SeasonDetailsScreenDestination
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeasonDetailsScreenArgs(
    val tvSeriesId: Int,
    val seasonNumber: Int,
    val startRoute: String
) : Parcelable

@Destination(navArgsDelegate = SeasonDetailsScreenArgs::class)
@Composable
fun SeasonDetailsScreen(
    viewModel: SeasonDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    backStackEntry: NavBackStackEntry
) {
    val navArgs: SeasonDetailsScreenArgs = SeasonDetailsScreenDestination.argsFrom(backStackEntry)

    val context = LocalContext.current

    val seasonDetails by viewModel.seasonDetails.collectAsState()
    val videos by viewModel.videos.collectAsState()
    val episodesCount by viewModel.episodeCount.collectAsState()
    val episodeStills by viewModel.episodeStills.collectAsState()

    val lazyState = rememberLazyListState()

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
        LazyColumn(
            state = lazyState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                PresentableDetailsTopSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    presentable = seasonDetails
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        seasonDetails?.seasonNumber?.let { number ->
                            LabeledText(
                                label = stringResource(R.string.season_details_season_number_label),
                                text = number.toString()
                            )
                        }
                        episodesCount?.let { count ->
                            LabeledText(
                                label = stringResource(R.string.season_details_episodes_count_label),
                                text = count.toString()
                            )
                        }
                        seasonDetails?.airDate?.let { date ->
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
                    targetState = seasonDetails
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
                            ExpandableText(
                                modifier = Modifier.fillMaxSize(),
                                text = details.overview
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
                    targetState = videos
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

            seasonDetails?.episodes?.let { episodes ->
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

                    var expanded by rememberSaveable(key = episode.id.toString()) {
                        mutableStateOf(false)
                    }

                    val stills by derivedStateOf {
                        episodeStills.getOrElse(
                            episode.episodeNumber,
                            defaultValue = { emptyList() })
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
                            viewModel.getEpisodeStills(episode.episodeNumber)
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
                    IconButton(
                        onClick = {
                            navigator.popBackStack(navArgs.startRoute, inclusive = false)
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