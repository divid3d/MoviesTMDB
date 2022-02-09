package com.example.moviesapp.ui.screens.discoverMovies.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.R
import com.example.moviesapp.model.FilterState
import com.example.moviesapp.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterModalBottomSheetContent(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    filterState: FilterState,
    onCloseClick: () -> Unit = {},
    onSaveFilterClick: (FilterState) -> Unit = {}
) {
    var currentFilterState by remember(filterState, sheetState.isVisible) {
        mutableStateOf(filterState)
    }

    val enableSaveButton = currentFilterState != filterState

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier.width(64.dp),
            color = Color.White.copy(0.3f),
            thickness = 4.dp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close filter",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.movie_filter_bottom_sheet_genres_section_label),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                GenresSelector(
                    modifier = Modifier.fillMaxWidth(),
                    genres = currentFilterState.availableGenres,
                    selectedGenres = currentFilterState.selectedGenres,
                    onGenreClick = { genre ->
                        val selectedGenres = currentFilterState.selectedGenres.run {
                            if (genre in this) {
                                minus(genre)
                            } else {
                                plus(genre)
                            }
                        }

                        currentFilterState = currentFilterState.copy(
                            selectedGenres = selectedGenres
                        )
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.movie_filter_bottom_sheet_score_section_label),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                VoteRangeSlider(
                    modifier = Modifier.fillMaxWidth(),
                    voteRange = currentFilterState.voteRange,
                    onCurrentVoteRangeChange = { voteRange ->
                        currentFilterState = currentFilterState.copy(
                            voteRange = currentFilterState.voteRange.copy(
                                current = voteRange
                            )
                        )
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.sort_type_release_date_label),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                DateRangeSelector(
                    modifier = Modifier.fillMaxWidth(),
                    fromDate = currentFilterState.releaseDateRange.from,
                    toDate = currentFilterState.releaseDateRange.to,
                    onFromDateChanged = { date ->
                        currentFilterState = currentFilterState.copy(
                            releaseDateRange = currentFilterState.releaseDateRange.copy(
                                from = date
                            )
                        )
                    },
                    onToDateChanged = { date ->
                        currentFilterState = currentFilterState.copy(
                            releaseDateRange = currentFilterState.releaseDateRange.copy(
                                to = date
                            )
                        )
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.movie_filter_bottom_sheet_other_section_label),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                LabeledSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.movie_filter_bottom_sheet_posters_switch_text),
                    checked = currentFilterState.showOnlyWithPoster,
                    onCheckedChanged = { show ->
                        currentFilterState = currentFilterState.copy(
                            showOnlyWithPoster = show
                        )
                    }
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    currentFilterState = currentFilterState.clear()
                }) {
                Text(text = stringResource(R.string.movie_filter_bottom_sheet_clear_button_label))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = enableSaveButton,
                onClick = { onSaveFilterClick(currentFilterState) }) {
                Text(text = stringResource(R.string.movie_filter_bottom_sheet_save_button_label))
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}