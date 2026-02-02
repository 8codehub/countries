package com.countries.data.mapper

import com.countries.core.mapper.Mapper
import com.countries.data.local.entity.CountryEntity
import com.countries.domain.model.Country

class CountryToCountryEntityMapper : Mapper<Country, CountryEntity> {
    override fun map(from: Country) = CountryEntity(
        cca3 = from.id,
        name = from.name,
        capital = from.capital.orEmpty(),
        currencies = from.currenciesDisplay,
        flag = from.flagUrl.orEmpty(),
    )
}