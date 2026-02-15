package com.countries.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoute {

    @Serializable
    data object CountriesList : NavigationRoute()

    @Serializable
    data class CountryDetails(
        val id: String? = null
    ) : NavigationRoute()
}
