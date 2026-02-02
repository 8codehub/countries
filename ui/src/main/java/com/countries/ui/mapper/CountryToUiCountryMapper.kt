package com.countries.ui.mapper

import com.countries.core.mapper.Mapper
import com.countries.domain.model.Country
import com.countries.ui.model.UiCountry

class CountryToUiCountryMapper : Mapper<Country, UiCountry> {
    override fun map(from: Country) = UiCountry(
        id = from.id,
        name = from.name,
        flagUrl = from.flagUrl
    )
}