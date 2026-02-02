package com.countries.domain.usecase

import com.countries.domain.model.Country
import com.countries.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCountryDetailsUseCaseImpl @Inject constructor(
    private val repository: CountriesRepository
) : ObserveCountryDetailsUseCase {

    override operator fun invoke(id: String): Flow<Country?> {
        return repository.observeCountry(id)
    }
}