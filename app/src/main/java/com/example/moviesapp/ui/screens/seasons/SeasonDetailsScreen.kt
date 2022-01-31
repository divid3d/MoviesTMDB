package com.example.moviesapp.ui.screens.seasons

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.moviesapp.R
import com.example.moviesapp.model.Episode
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.other.formatted
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.ErrorDialog
import com.example.moviesapp.ui.components.LabeledText
import com.example.moviesapp.ui.components.PresentableDetailsTopSection
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.screens.movies.components.OverviewSection
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SeasonDetailsScreen(
    seasonInfo: SeasonInfo,
    navigator: DestinationsNavigator,
    startRoute: String = TvScreenDestination.route
) {
    val viewModel: SeasonDetailsViewModel = hiltViewModel()

    val seasonDetails by viewModel.seasonDetails.collectAsState()
    val episodesCount by viewModel.episodeCount.collectAsState()

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


            seasonDetails?.let { details ->
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(bottom = MaterialTheme.spacing.large)
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
                        OverviewSection(
                            overview = details.overview
                        )
                    }
                }

                itemsIndexed(details.episodes) { index, episode ->
                    val bottomPadding = if (index < details.episodes.count() - 1) {
                        MaterialTheme.spacing.medium
                    } else {
                        MaterialTheme.spacing.default
                    }

                    var expanded by rememberSaveable(key = episode.id.toString()) {
                        mutableStateOf(false)
                    }

                    EpisodeChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium)
                            .padding(bottom = bottomPadding),
                        episode = episode,
                        expanded = expanded
                    ) {
                        expanded = !expanded
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                }
            }
        }

        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.season_details_appbar_label),
            backgroundColor = Black500,
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


@Composable
fun EpisodeChip(
    modifier: Modifier = Modifier,
    episode: Episode,
    expanded: Boolean = false,
    onClick: () -> Unit = {}
) {
    val iconRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = episode.name,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )

                    episode.airDate?.let { date ->
                        Text(
                            text = date.formatted(),
                            style = TextStyle(fontWeight = FontWeight.Thin)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Icon(
                    modifier = Modifier.rotate(iconRotation),
                    imageVector = Icons.Filled.ArrowDropDown,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = if (expanded) "collapse" else "expand"
                )
            }

            AnimatedVisibility(
                enter = fadeIn(),
                exit = fadeOut(),
                visible = expanded
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    Text(text = episode.overview, style = TextStyle(fontSize = 12.sp))
                    Image(
                        painter = rememberImagePainter(
                            data = episode.stillUrl,
                            builder = {
                                size(OriginalSize)
                                crossfade(true)
                            }
                        ),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

    }
}