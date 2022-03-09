package com.example.moviesapp.ui.screens.details

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.moviesapp.R
import com.example.moviesapp.model.ExternalId
import com.example.moviesapp.model.MediaType
import com.example.moviesapp.other.ifNotNullAndEmpty
import com.example.moviesapp.other.openExternalId
import com.example.moviesapp.ui.components.dialogs.ErrorDialog
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.components.others.SectionDivider
import com.example.moviesapp.ui.components.sections.ExternalIdsSection
import com.example.moviesapp.ui.screens.destinations.*
import com.example.moviesapp.ui.screens.details.components.CreditsList
import com.example.moviesapp.ui.screens.details.components.PersonDetailsInfoSection
import com.example.moviesapp.ui.screens.details.components.PersonDetailsTopContent
import com.example.moviesapp.ui.screens.details.components.PersonProfileImage
import com.example.moviesapp.ui.theme.Black300
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalAnimationApi::class)
object PersonDetailsScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            TvScreenDestination.route,
            MoviesScreenDestination.route,
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
            MoviesScreenDestination.route,
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
data class PersonDetailsScreenArgs(
    val personId: Int,
    val startRoute: String
) : Parcelable

@Destination(
    navArgsDelegate = PersonDetailsScreenArgs::class,
    style = PersonDetailsScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.PersonDetailsScreen(
    viewModel: PersonDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val onBackClicked: () -> Unit = { navigator.navigateUp() }
    val onCloseClicked: () -> Unit = {
        navigator.popBackStack(uiState.startRoute, inclusive = false)
    }
    val onExternalIdClicked = { id: ExternalId ->
        openExternalId(
            context = context,
            externalId = id
        )
    }
    val onMediaClicked = { mediaType: MediaType, id: Int ->
        val destination = when (mediaType) {
            MediaType.Movie -> {
                MovieDetailsScreenDestination(
                    movieId = id,
                    startRoute = uiState.startRoute
                )
            }

            MediaType.Tv -> {
                TvSeriesDetailsScreenDestination(
                    tvSeriesId = id,
                    startRoute = uiState.startRoute
                )
            }

            else -> null
        }

        if (destination != null) {
            navigator.navigate(destination)
        }
    }

    PersonDetailsScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onCloseClicked = onCloseClicked,
        onExternalIdClicked = onExternalIdClicked,
        onMediaClicked = onMediaClicked
    )
}

@Composable
fun PersonDetailsScreenContent(
    uiState: PersonDetailsScreenUiState,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onExternalIdClicked: (id: ExternalId) -> Unit,
    onMediaClicked: (type: MediaType, id: Int) -> Unit
) {
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.error) {
        showErrorDialog = uiState.error != null
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
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
                    profilePath = uiState.details?.profilePath
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Black300,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(MaterialTheme.spacing.small)
                    ) {
                        PersonDetailsTopContent(
                            modifier = Modifier.fillMaxWidth(),
                            personDetails = uiState.details
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    uiState.externalIds?.let { ids ->
                        ExternalIdsSection(
                            modifier = Modifier.fillMaxWidth(),
                            externalIds = ids,
                            onExternalIdClick = onExternalIdClicked
                        )
                    }
                }
            }

            PersonDetailsInfoSection(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                personDetails = uiState.details
            )

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = uiState.credits?.cast
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
                            credits = members,
                            onCreditsClick = onMediaClicked
                        )
                    }
                }
            }

            Crossfade(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                targetState = uiState.credits?.crew
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
                            credits = members,
                            onCreditsClick = onMediaClicked
                        )
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
                    IconButton(onClick = onCloseClicked) {
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