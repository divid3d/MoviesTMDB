package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.LocalImageLoader
import com.example.moviesapp.model.SnackBarEvent
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.safeNavigate
import com.example.moviesapp.ui.components.BottomBar
import com.example.moviesapp.ui.screens.NavGraphs
import com.example.moviesapp.ui.screens.destinations.FavouritesScreenDestination
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

val LocalImageUrlParser = staticCompositionLocalOf<ImageUrlParser?> { null }

@AndroidEntryPoint
@OptIn(InternalCoroutinesApi::class, ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val context = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current
            val imageUrlParser by mainViewModel.imageUrlParser.collectAsState()

            val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
            val snackBarEvent: SnackBarEvent? by mainViewModel.networkSnackBarEvent.collectAsState()

            //val useDarkIcons = MaterialTheme.colors.isLight
            val navController = rememberNavController()
            val systemUiController = rememberSystemUiController()

            var currentRoute: String? by rememberSaveable {
                mutableStateOf(null)
            }

            var backQueueRoutes: List<String?> by rememberSaveable {
                mutableStateOf(emptyList())
            }

            navController.apply {
                addOnDestinationChangedListener { controller, _, _ ->
                    currentRoute = controller.currentBackStackEntry?.destination?.route
                    backQueueRoutes = controller.backQueue.map { entry -> entry.destination.route }
                }
                addOnDestinationChangedListener { _, _, _ ->
                    keyboardController?.hide()
                }
            }

            val showBottomBar by derivedStateOf {
                currentRoute in setOf(
                    null,
                    MoviesScreenDestination.route,
                    TvScreenDestination.route,
                    FavouritesScreenDestination.route,
                    SearchScreenDestination.route
                )
            }

            LaunchedEffect(snackBarEvent) {
                snackBarEvent?.let { event ->
                    val result = snackBarHostState.showSnackbar(
                        message = event.message
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            /* action has been performed */
                        }
                        SnackbarResult.Dismissed -> {
                            /* dismissed, no action needed */
                        }
                    }
                }
            }

            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Black500,
                    darkIcons = false
                )
            }

            CompositionLocalProvider(
                LocalImageLoader provides ImageLoader(context),
                LocalImageUrlParser provides imageUrlParser
            ) {
                MoviesAppTheme {
                    // A surface container using the 'background' color from the theme
                    ProvideWindowInsets {
                        Scaffold(
                            scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState),
                            bottomBar = {
                                BottomBar(
                                    modifier = Modifier.navigationBarsPadding(),
                                    currentRoute = currentRoute,
                                    backQueueRoutes = backQueueRoutes,
                                    visible = showBottomBar
                                ) { route ->
                                    navController.safeNavigate(route)
                                }
                            }
                        ) { innerPadding ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        bottom = if (showBottomBar) {
                                            innerPadding.calculateBottomPadding()
                                        } else {
                                            MaterialTheme.spacing.default
                                        }
                                    ),
                                color = MaterialTheme.colors.background
                            ) {
                                DestinationsNavHost(
                                    navGraph = NavGraphs.root,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}