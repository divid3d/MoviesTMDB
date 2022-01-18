package com.example.moviesapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.example.moviesapp.R
import com.example.moviesapp.other.safeNavigate
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    var currentRoute: String? by remember {
        mutableStateOf(null)
    }

    navController.apply {
        addOnDestinationChangedListener { controller, _, _ ->
            currentRoute = controller.currentBackStackEntry?.destination?.route
        }
    }

    val showBottomBar by derivedStateOf {
        currentRoute in setOf(
            MoviesScreenDestination.route,
            TvScreenDestination.route,
            SearchScreenDestination.route
        )
    }

    AnimatedVisibility(
        visible = showBottomBar,
        enter = slideIn(
            animationSpec = spring(),
            initialOffset = { size -> IntOffset(0, size.height) }
        ),
        exit = slideOut(
            animationSpec = spring(),
            targetOffset = { size -> IntOffset(0, size.height) }
        )
    ) {
        BottomNavigation(
            modifier = modifier
        ) {
            BottomBarNavigationItem(
                selected = currentRoute == MoviesScreenDestination.route,
                onClick = {
                    navController.safeNavigate(MoviesScreenDestination.route)
                },
                label = stringResource(R.string.movies_label),
                icon = R.drawable.ic_outline_movie_24
            )
            BottomBarNavigationItem(
                selected = currentRoute == TvScreenDestination.route,
                onClick = {
                    navController.safeNavigate(TvScreenDestination.route)
                },
                label = stringResource(R.string.tv_series_label),
                icon = R.drawable.ic_outline_tv_24
            )
            BottomBarNavigationItem(
                selected = currentRoute == SearchScreenDestination.route,
                onClick = {
                    navController.safeNavigate(SearchScreenDestination.route)
                },
                label = stringResource(R.string.search_label),
                icon = R.drawable.ic_outline_search_24
            )
        }
    }
}

@Composable
fun RowScope.BottomBarNavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    label: String,
    @DrawableRes icon: Int,
    selectedColor: Color = LocalContentColor.current,
    onClick: () -> Unit = {}
) {
    val iconTint by animateColorAsState(
        targetValue = if (selected) {
            selectedColor
        } else {
            selectedColor.copy(alpha = ContentAlpha.medium)
        }
    )

    val iconScale by animateFloatAsState(targetValue = if (selected) 1.1f else 1f)

    BottomNavigationItem(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        icon = {
            Image(
                modifier = Modifier.scale(iconScale),
                painter = painterResource(icon),
                contentDescription = "search",
                colorFilter = ColorFilter.tint(iconTint)
            )
        }
    )
}