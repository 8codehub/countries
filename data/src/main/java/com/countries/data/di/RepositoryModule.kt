package com.countries.data.di

import com.countries.core.mapper.Mapper
import com.countries.data.local.dao.CountryDao
import com.countries.data.local.entity.CountryEntity
import com.countries.data.remote.CountriesApi
import com.countries.data.remote.dto.CountryDto
import com.countries.data.repository.CountriesRepositoryImpl
import com.countries.domain.model.Country
import com.countries.domain.repository.CountriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Provides
    @Singleton
    fun provideCountriesRepository(
        api: CountriesApi,
        dao: CountryDao,
        entityToDomain: Mapper<CountryEntity, Country>,
        dtoToEntity: Mapper<CountryDto, CountryEntity>
    ): CountriesRepository {
        return CountriesRepositoryImpl(
            api = api,
            dao = dao,
            entityToDomain = entityToDomain,
            dtoToEntity = dtoToEntity
        )
    }
}
