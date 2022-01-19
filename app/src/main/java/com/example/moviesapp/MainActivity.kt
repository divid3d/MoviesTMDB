package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.LocalImageLoader
import com.example.moviesapp.model.SnackBarEvent
import com.example.moviesapp.ui.components.BottomBar
import com.example.moviesapp.ui.screens.NavGraphs
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi


@AndroidEntryPoint
@OptIn(InternalCoroutinesApi::class)
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val context = LocalContext.current
            val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
            val snackBarEvent: SnackBarEvent? by mainViewModel.networkSnackBarEvent.collectAsState()

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

            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()

            val useDarkIcons = MaterialTheme.colors.isLight

            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setStatusBarColor(
                    color = Black500,
                    darkIcons = false
                )
            }

            CompositionLocalProvider(LocalImageLoader provides ImageLoader(context)) {
                MoviesAppTheme {
                    // A surface container using the 'background' color from the theme
                    ProvideWindowInsets {
                        Scaffold(
                            scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState),
                            bottomBar = {
                                BottomBar(
                                    modifier = Modifier.navigationBarsPadding(),
                                    navController = navController
                                )
                            }
                        ) { innerPadding ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = innerPadding.calculateBottomPadding()),
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