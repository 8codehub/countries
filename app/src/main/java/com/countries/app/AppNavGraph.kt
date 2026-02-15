package com.countries.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.countries.ui.details.CountryDetailsScreen
import com.countries.ui.list.CountriesListScreen
import com.countries.ui.navigation.NavigationRoute.CountriesList
import com.countries.ui.navigation.NavigationRoute.CountryDetails


@Composable
fun CountriesNavGraph(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = CountriesList
    ) {
        composable<CountriesList> {
            CountriesListScreen(
                navigateTo = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable<CountryDetails> {
            CountryDetailsScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}
