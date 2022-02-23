package com.example.moviesapp.ui.screens.details

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.moviesapp.R
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.other.ifNotNullAndEmpty
import com.example.moviesapp.other.openExternalId
import com.example.moviesapp.ui.components.AppBar
import com.example.moviesapp.ui.components.ExternalIdsSection
import com.example.moviesapp.ui.components.SectionDivider
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.screens.destinations.MovieDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.PersonDetailsScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvSeriesDetailsScreenDestination
import com.example.moviesapp.ui.screens.details.components.CreditsList
import com.example.moviesapp.ui.screens.details.components.PersonDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.PersonDetailsTopContent
import com.example.moviesapp.ui.screens.details.components.PersonProfileImage
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonDetailsScreenArgs(
    val personId: Int,
    val startRoute: String
) : Parcelable

@Destination(navArgsDelegate = PersonDetailsScreenArgs::class)
@Composable
fun PersonDetailsScreen(
    viewModel: PersonDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    backStackEntry: NavBackStackEntry
) {
    val navArgs: PersonDetailsScreenArgs by derivedStateOf {
        PersonDetailsScreenDestination.argsFrom(backStackEntry)
    }
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
                    startRoute = navArgs.startRoute
                )
            }

            MediaType.Tv -> {
                TvSeriesDetailsScreenDestination(
                    tvSeriesId = id,
                    startRoute = navArgs.startRoute
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
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
                    profilePath = details?.profilePath
                )

                Column(modifier = Modifier
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
                    PersonDetailsTopContent(
                        modifier = Modifier.fillMaxWidth(),
                        personDetails = details
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    externalIds?.let { ids ->
                        ExternalIdsSection(
                            modifier = Modifier.fillMaxWidth(),
                            externalIds = ids
                        ) { externalId ->
                            openExternalId(
                                context = context, externalId = externalId
                            )
                        }
                    }
                }
            }

            PersonDetailsInfoSection(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                personDetails = details
            )

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = cast
            ) { cast ->
                cast.ifNotNullAndEmpty { members ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        CreditsList(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.person_details_screen_cast),
                            credits = members
                        ) { mediaType, id -> navigateToDetails(mediaType, id) }
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = crew
            ) { crew ->
                crew.ifNotNullAndEmpty { members ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        SectionDivider(modifier = Modifier.fillMaxWidth())
                        CreditsList(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.person_details_screen_crew),
                            credits = members
                        ) { mediaType, id -> navigateToDetails(mediaType, id) }
                    }
                }
            }

            Spacer(modifier = Modifier.navigationBarsHeight(additional = MaterialTheme.spacing.large))
        }

        AppBar(
            modifier = Modifier.align(Alignment.TopCenter),
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
                        navigator.popBackStack(navArgs.startRoute, inclusive = false)
                    }) {
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