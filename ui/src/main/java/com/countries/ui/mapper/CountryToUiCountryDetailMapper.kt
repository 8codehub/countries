package com.countries.ui.mapper

import com.countries.core.mapper.Mapper
import com.countries.domain.model.Country
import com.countries.ui.model.UiCountryDetail

class CountryToUiUiCountryDetailMapper : Mapper<Country, UiCountryDetail> {
    override fun map(from: Country) = UiCountryDetail(
        countryFlag = from.flagUrl.orEmpty(),
        countryName = from.name,
        capitalName = from.capital.orEmpty(),
        countryCurrencies = from.currenciesDisplay
    )
}