package com.countries.domain.repository

import com.countries.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {

    fun observeCountries(): Flow<List<Country>>

    suspend fun refreshCountries(): Result<Unit>

    fun observeCountry(id: String): Flow<Country?>

    suspend fun searchCountries(query: String): Result<List<Country>>
}