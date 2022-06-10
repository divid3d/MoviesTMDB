package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.compose.LocalImageLoader
import com.example.moviesapp.data.ConfigDataSource
import com.example.moviesapp.model.SnackBarEvent
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.safeNavigate
import com.example.moviesapp.ui.components.others.BottomBar
import com.example.moviesapp.ui.screens.NavGraphs
import com.example.moviesapp.ui.screens.destinations.FavouritesScreenDestination
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

val LocalImageUrlParser = staticCompositionLocalOf<ImageUrlParser?> { null }

@AndroidEntryPoint
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
    ExperimentalComposeUiApi::class
)
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var configDataSource: ConfigDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition(
                condition = { configDataSource.isInitialized.value }
            )
        }

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel(this)

            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current

            val keyboardController = LocalSoftwareKeyboardController.current
            val imageUrlParser by mainViewModel.imageUrlParser.collectAsState()

            val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
            val snackBarEvent: SnackBarEvent? by mainViewModel.networkSnackBarEvent.collectAsState()

            //val useDarkIcons = MaterialTheme.colors.isLight
            val navController = rememberAnimatedNavController()
            val navHostEngine = rememberAnimatedNavHostEngine(
                navHostContentAlignment = Alignment.TopCenter,
                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = { fadeIn(animationSpec = tween(500)) },
                    exitTransition = { fadeOut(animationSpec = tween(500)) }
                )
            )

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
                    snackBarHostState.showSnackbar(
                        message = getString(event.messageStringRes)
                    )
                }
            }

            LaunchedEffect(lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    Timber.d("Update locale")

                    mainViewModel.updateLocale()
                }
            }

            CompositionLocalProvider(LocalImageUrlParser provides imageUrlParser) {
                MoviesAppTheme {
                    val navigationBarColor = MaterialTheme.colors.surface

                    SideEffect {
                        systemUiController.setStatusBarColor(
                            color = Black500,
                            darkIcons = false
                        )

                        systemUiController.setNavigationBarColor(
                            color = navigationBarColor,
                            darkIcons = false
                        )
                    }

                    Scaffold(
                        scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState),
                        bottomBar = {
                            BottomBar(
                                modifier = Modifier.navigationBarsPadding(),
                                currentRoute = currentRoute,
                                backQueueRoutes = backQueueRoutes,
                                visible = showBottomBar
                            ) { route ->
                                navController.safeNavigate(
                                    route = route,
                                    onSameRouteSelected = { sameRoute ->
                                        mainViewModel.onSameRouteSelected(sameRoute)
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    bottom = if (showBottomBar) {
                                        innerPadding.calculateBottomPadding()
                                    } else MaterialTheme.spacing.default
                                ),
                            color = MaterialTheme.colors.background
                        ) {
                            DestinationsNavHost(
                                navGraph = NavGraphs.root,
                                engine = navHostEngine,
                                navController = navController,
                                dependenciesContainerBuilder = {
                                    dependency(mainViewModel)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}