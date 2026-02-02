package com.countries.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.countries.ui.details.CountryDetailsScreen
import com.countries.ui.list.CountriesListScreen
import com.countries.ui.navigation.Destinations
import com.countries.ui.navigation.NavArgs

@Composable
fun CountriesNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Destinations.CountriesListRoute
) {

    /***
     * Note:
     * This navigation approach is probably not the best one.
     * It would be better to use a type-safe navigation setup,
     * similar to what I implemented in ChatNote:
     *
     * Main navigation example:
     * https://github.com/8codehub/ChatNote/blob/main/app/src/main/java/com/chatnote/app/MainActivity.kt
     *
     * With that approach, passing arguments directly into the ViewModel becomes much cleaner,
     * like in this example:
     * https://github.com/8codehub/ChatNote/blob/main/feature/direct-notes/directnotes-ui/src/main/java/com/chatnote/directnotesui/DirectNotesViewModel.kt
     *
     * Unfortunately, due to the limited time of this assignment, I didn’t manage to apply
     * the same navigation structure here.
     ***/
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Destinations.CountriesListRoute) {
            CountriesListScreen(
                onCountryClick = { country ->
                    navController.navigate(Destinations.countryDetails(country.id))
                }
            )
        }

        composable(
            route = Destinations.CountryDetailsRoute,
            arguments = listOf(
                navArgument(NavArgs.CountryId) { type = NavType.StringType }
            )
        ) { entry ->
            val countryId = requireNotNull(entry.arguments?.getString(NavArgs.CountryId))
            CountryDetailsScreen(
                countryId = countryId,
                onBack = navController::popBackStack
            )
        }
    }
}
