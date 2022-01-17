package com.example.moviesapp.other

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.safeNavigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val currentRoute = currentBackStackEntry?.destination?.route

    if (currentRoute == route) {
        return
    }

    val isInBackstack = backQueue.map { entry -> entry.destination }
        .any { navDestination -> navDestination.route == route }

    if (isInBackstack) {
        popBackStack(
            route = route,
            inclusive = false
        )
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}