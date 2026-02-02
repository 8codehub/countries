package com.countries.data.mapper

import com.countries.core.mapper.Mapper
import com.countries.data.local.entity.CountryEntity
import com.countries.domain.model.Country

class CountryEntityToCountryMapper : Mapper<CountryEntity, Country> {
    override fun map(from: CountryEntity) = Country(
        id = from.cca3,
        name = from.name,
        capital = from.capital,
        currenciesDisplay = from.currencies,
        flagUrl = from.flag,
    )
}
