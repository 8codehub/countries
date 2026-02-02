package com.countries.domain.usecase

import com.countries.domain.repository.CountriesRepository
import javax.inject.Inject

class RefreshCountriesUseCaseImpl @Inject constructor(
    private val repository: CountriesRepository
) : RefreshCountriesUseCase {

    override suspend operator fun invoke(): Result<Unit> =
        repository.refreshCountries()
}