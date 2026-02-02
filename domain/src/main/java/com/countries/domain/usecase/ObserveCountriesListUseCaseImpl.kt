package com.countries.domain.usecase

import com.countries.domain.model.Country
import com.countries.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCountriesListUseCaseImpl @Inject constructor(
    private val repository: CountriesRepository
) : ObserveCountriesListUseCase {

    override operator fun invoke(): Flow<List<Country>> =
        repository.observeCountries()
}