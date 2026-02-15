package com.countries.data.mapper

import com.countries.core.mapper.Mapper
import com.countries.data.local.entity.CountryEntity
import com.countries.data.remote.dto.CountryDto
import javax.inject.Inject

internal class CountryDtoToCountryEntityMapper @Inject constructor() :
    Mapper<CountryDto, CountryEntity> {
    override fun map(from: CountryDto): CountryEntity {

        return CountryEntity(
            cca3 = from.cca3,
            name = from.name.common,
            capital = from.capital.firstOrNull().orEmpty(),
            currencies = from.currencies.toString(),
            flag = from.flags?.png.toString(),
        )
    }
}