package com.example.moviesapp.ui.screens.scanner

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.moviesapp.R
import com.example.moviesapp.other.Roi
import com.example.moviesapp.ui.components.others.AppBar
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.scanner.components.ScanResultModalBottomSheetContent
import com.example.moviesapp.ui.screens.scanner.components.TitleScanner
import com.example.moviesapp.ui.theme.Black500
import com.example.moviesapp.ui.theme.White300
import com.example.moviesapp.ui.theme.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
object ScannerScreenTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.destination.route) {
            SearchScreenDestination.route -> slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return when (initialState.destination.route) {
            SearchScreenDestination.route -> slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return when (targetState.destination.route) {
            SearchScreenDestination.route -> slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Down,
                animationSpec = tween(300)
            )
            else -> null
        }
    }
}

@Destination(
    style = ScannerScreenTransitions::class
)
@Composable
fun AnimatedVisibilityScope.ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<String>
) {
    val uiState by viewModel.uiState.collectAsState()

    val onBackClicked: () -> Unit = {
        navigator.navigateUp()
    }

    val onAcceptClicked = {
        val result = uiState.scanResult
        if (result is ScanResult.Success) {
            resultNavigator.navigateBack(result.text)
        }
    }

    ScannerScreenContent(
        uiState = uiState,
        onBackClicked = onBackClicked,
        onBitmapCaptured = viewModel::onBitmapCaptured,
        onAcceptClicked = onAcceptClicked
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScannerScreenContent(
    uiState: ScannerScreenUiState,
    onBackClicked: () -> Unit = {},
    onBitmapCaptured: (Bitmap, Float, Roi?) -> Unit,
    onAcceptClicked: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val onCloseBottomSheetClick: () -> Unit = {
        if (sheetState.isVisible) {
            coroutineScope.launch {
                sheetState.hide()
            }
        }
    }

    val errorText = uiState.validationErrorResId?.let {
        stringResource(it)
    }

    LaunchedEffect(uiState.scanResult) {
        when (uiState.scanResult) {
            is ScanResult.Success -> {
                if (!sheetState.isVisible) {
                    sheetState.show()
                }
            }
            else -> {
                if (sheetState.isVisible) {
                    sheetState.hide()
                }
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        scrimColor = Black500,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            ScanResultModalBottomSheetContent(
                modifier = Modifier.fillMaxWidth(),
                scanResult = uiState.scanResult,
                onCloseClick = onCloseBottomSheetClick,
                onRejectClicked = onCloseBottomSheetClick,
                onAcceptClicked = onAcceptClicked
            )
        }
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            when (cameraPermissionState.status) {
                PermissionStatus.Granted -> {
                    TitleScanner(
                        modifier = Modifier.fillMaxSize(),
                        isScanningInProgress = uiState.scanningInProgress,
                        errorText = errorText,
                        onBitmapCaptured = onBitmapCaptured
                    )
                }
                is PermissionStatus.Denied -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.medium),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.camera_permission_rationale_info),
                            color = White300,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        OutlinedButton(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                            Text(
                                modifier = Modifier.animateContentSize(),
                                text = stringResource(R.string.camera_permission_rationale_request_permission_button_label)
                            )
                        }
                    }
                }
            }

            AppBar(
                modifier = Modifier.align(Alignment.TopCenter),
                title = null,
                action = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "go back",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            )
        }
    }
}