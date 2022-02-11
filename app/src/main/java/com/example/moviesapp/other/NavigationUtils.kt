package com.example.moviesapp.other

import androidx.navigation.NavController
import com.example.moviesapp.ui.screens.destinations.MoviesScreenDestination

fun NavController.safeNavigate(route: String) {
    val currentRoute = currentBackStackEntry?.destination?.route

    if (currentRoute == route) {
        return
    }

    val isInBackstack = backQueue.map { entry -> entry.destination.route }
        .any { it == route }

    if (isInBackstack) {
        popBackStack(
            route = route,
            inclusive = false
        )
    } else {
        navigate(route) {
            popUpTo(MoviesScreenDestination.route)
        }
    }
}