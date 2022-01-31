package com.example.moviesapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.moviesapp.R
import com.example.moviesapp.other.safeNavigate
import com.example.moviesapp.ui.screens.destinations.FavouritesScreenDestination
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination
import com.example.moviesapp.ui.screens.destinations.SearchScreenDestination
import com.example.moviesapp.ui.screens.destinations.TvScreenDestination


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    visible: Boolean = true,
    currentRoute: String? = null
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
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
                contentDescription = "movies",
                unselectedIcon = R.drawable.ic_outline_movie_24,
                selectedIcon = R.drawable.ic_baseline_movie_24
            )
            BottomBarNavigationItem(
                selected = currentRoute == TvScreenDestination.route,
                onClick = {
                    navController.safeNavigate(TvScreenDestination.route)
                },
                label = stringResource(R.string.tv_series_label),
                contentDescription = "tv series",
                selectedIcon = R.drawable.ic_outline_tv_24
            )
            BottomBarNavigationItem(
                selected = currentRoute == FavouritesScreenDestination.route,
                onClick = {
                    navController.safeNavigate(FavouritesScreenDestination.route)
                },
                label = stringResource(R.string.favourites_label),
                contentDescription = "favourites",
                unselectedIcon = R.drawable.ic_heart_outline,
                selectedIcon = R.drawable.ic_heart
            )
            BottomBarNavigationItem(
                selected = currentRoute == SearchScreenDestination.route,
                onClick = {
                    navController.safeNavigate(SearchScreenDestination.route)
                },
                label = stringResource(R.string.search_label),
                contentDescription = "search",
                selectedIcon = R.drawable.ic_outline_search_24
            )
        }
    }
}

@Composable
fun RowScope.BottomBarNavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    label: String,
    @DrawableRes selectedIcon: Int,
    @DrawableRes unselectedIcon: Int = selectedIcon,
    contentDescription: String? = null,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
    onClick: () -> Unit = {}
) {
    val iconScale by animateFloatAsState(targetValue = if (selected) 1.1f else 1f)

    BottomNavigationItem(
        modifier = modifier.scale(iconScale),
        selected = selected,
        onClick = onClick,
        selectedContentColor = selectedColor,
        unselectedContentColor = unselectedColor,
        label = { Text(label) },
        icon = {
            Image(
                painter = painterResource(if (selected) selectedIcon else unselectedIcon),
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(LocalContentColor.current)
            )
        }
    )
}