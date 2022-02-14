package com.example.moviesapp.ui.screens.person

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.other.formatted
import com.example.moviesapp.other.openExternalId
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.ExpandableText
import com.example.moviesapp.ui.components.ExternalIdsSection
import com.example.moviesapp.ui.components.LabeledText
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.person.components.CreditsList
import com.example.moviesapp.ui.screens.person.components.PersonProfileImage
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.statusBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun PersonDetailsScreen(
    viewModel: PersonDetailsViewModel = hiltViewModel(),
    personId: Int,
    navigator: DestinationsNavigator,
    startRoute: String = MoviesScreenDestination.route
) {
    val context = LocalContext.current

    val details by viewModel.personDetails.collectAsState()
    val externalIds by viewModel.externalIds.collectAsState()
    val cast by viewModel.cast.collectAsState()
    val crew by viewModel.crew.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    val error: String? by viewModel.error.collectAsState()

    LaunchedEffect(error) {
        showErrorDialog = error != null
    }

    BackHandler(showErrorDialog) {
        showErrorDialog = false
    }

    if (showErrorDialog) {
        ErrorDialog(onDismissRequest = {
            showErrorDialog = false
        }, onConfirmClick = {
            showErrorDialog = false
        })
    }

    val navigateToDetails = { mediaType: MediaType, id: Int ->
        val destination = when (mediaType) {
            MediaType.Movie -> {
                MovieDetailsScreenDestination(
                    movieId = id,
                    startRoute = startRoute
                )
            }

            MediaType.Tv -> {
                TvSeriesDetailsScreenDestination(
                    tvSeriesId = id,
                    startRoute = startRoute
                )
            }

            else -> null
        }

        if (destination != null) {
            navigator.navigate(destination)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 56.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                details?.let { details ->

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        val (profileImageRef, contentRef) = createRefs()

                        PersonProfileImage(
                            modifier = Modifier.constrainAs(profileImageRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            },
                            profilePath = details.profilePath
                        )

                        Column(
                            modifier = Modifier
                                .constrainAs(contentRef) {
                                    start.linkTo(profileImageRef.end)
                                    end.linkTo(parent.end)
                                    top.linkTo(profileImageRef.top)
                                    bottom.linkTo(profileImageRef.bottom)

                                    height = Dimension.fillToConstraints
                                    width = Dimension.fillToConstraints
                                }
                                .padding(start = MaterialTheme.spacing.medium)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                            ) {
                                LabeledText(label = "Known for", text = details.knownFor)

                                details.birthPlace?.let { birthplace ->
                                    LabeledText(label = "Birthplace", text = birthplace)
                                }

                                details.birthday?.let { date ->
                                    LabeledText(label = "Birthsday", text = date.formatted())
                                }

                                details.deathDate?.let { date ->
                                    Text(text = date.formatted())
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
                        }
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                        text = details.name,
                        style = TextStyle(
                            color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold
                        )
                    )

                    ExpandableText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                        text = details.biography
                    )
                }
            }

            CreditsList(
                modifier = Modifier.fillMaxWidth(),
                title = "Grał w",
                list = cast
            ) { mediaType, id -> navigateToDetails(mediaType, id) }

            CreditsList(
                modifier = Modifier.fillMaxWidth(),
                title = "Uczestniczył w",
                list = crew
            ) { mediaType, id -> navigateToDetails(mediaType, id) }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }

        AppBar(modifier = Modifier.align(Alignment.TopCenter),
            title = stringResource(R.string.person_details_screen_appbar_label),
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
                    IconButton(onClick = {
                        navigator.popBackStack(startRoute, inclusive = false)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "close",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            })
    }

}