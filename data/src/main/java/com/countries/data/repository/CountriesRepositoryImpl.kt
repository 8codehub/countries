package com.countries.data.repository

import com.countries.core.mapper.Mapper
import com.countries.data.local.dao.CountryDao
import com.countries.data.local.entity.CountryEntity
import com.countries.data.remote.CountriesApi
import com.countries.data.remote.dto.CountryDto
import com.countries.domain.model.Country
import com.countries.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CountriesRepositoryImpl @Inject constructor(
    private val api: CountriesApi,
    private val dao: CountryDao,
    private val entityToDomain: Mapper<CountryEntity, Country>,
    private val dtoToEntity: Mapper<CountryDto, CountryEntity>
) : CountriesRepository {

    override fun observeCountries(): Flow<List<Country>> {
        return dao.observeCountries()
            .map { entities -> entities.map(entityToDomain::map) }
    }

    override fun observeCountry(id: String): Flow<Country?> {
        return dao.observeCountry(id)
            .map { entity -> entity?.let(entityToDomain::map) }
    }

    override suspend fun refreshCountries(): Result<Unit> {
        return runCatching {
            val response = api.getAll()
            if (!response.isSuccessful) error("HTTP ${response.code()}")

            val dtos = response.body().orEmpty()
            val entities = dtos.map(dtoToEntity::map)

            dao.insertAll(entities)
        }
    }

    /**
     * Note:
     * Since all required country data is loaded and stored locally,
     * the search feature is implemented by filtering the already displayed list
     * instead of querying the database for each user input.
     */
    override suspend fun searchCountries(query: String): Result<List<Country>> {
        return runCatching {
            dao.search(query).map(entityToDomain::map)
        }
    }
}
