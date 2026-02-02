package com.countries.ui.navigation

import android.net.Uri

object Routes {
    const val CountriesList = "countries_list"
    const val CountryDetails = "country_details"
}

object NavArgs {
    const val CountryId = "countryId"
}

object Destinations {

    const val CountriesListRoute = Routes.CountriesList

    const val CountryDetailsRoute = "${Routes.CountryDetails}/{${NavArgs.CountryId}}"

    fun countryDetails(countryId: String): String {
        return "${Routes.CountryDetails}/${Uri.encode(countryId)}"
    }
}