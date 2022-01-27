package com.example.moviesapp.ui.screens.seasons

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesapp.R
import com.example.moviesapp.model.Episode
import com.example.moviesapp.model.SeasonInfo
import com.example.moviesapp.other.formatted
import com.example.moviesapp.ui.components.AppBar
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

                items(details.episodes) { episode ->
                    EpisodeChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                        episode = episode
                    )
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
                        contentDescription = null
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
                            contentDescription = null
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
    episode: Episode
) {
    Box(modifier = modifier.border(border = BorderStroke(1.dp, color = Color.White))) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            Text(text = episode.name)
            Text(text = episode.overview)
        }
    }
}