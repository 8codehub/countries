package com.countries.domain.usecase

import com.countries.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface ObserveCountriesListUseCase {
    operator fun invoke(): Flow<List<Country>>
}

interface ObserveCountryDetailsUseCase {
    operator fun invoke(id: String): Flow<Country?>
}

interface RefreshCountriesUseCase {
    suspend operator fun invoke(): Result<Unit>
}