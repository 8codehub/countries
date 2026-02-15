package com.countries.data.di

import com.countries.core.mapper.Mapper
import com.countries.data.local.entity.CountryEntity
import com.countries.data.mapper.CountryDtoToCountryEntityMapper
import com.countries.data.mapper.CountryEntityToCountryMapper
import com.countries.data.mapper.CountryToCountryEntityMapper
import com.countries.data.remote.dto.CountryDto
import com.countries.domain.model.Country
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MapperModule {

    @Binds
    @Singleton
    abstract fun bindCountryDtoToCountryEntityMapper(
        impl: CountryDtoToCountryEntityMapper
    ): Mapper<CountryDto, CountryEntity>

    @Binds
    @Singleton
    abstract fun bindCountryEntityToCountryMapper(
        impl: CountryEntityToCountryMapper
    ): Mapper<CountryEntity, Country>

    @Binds
    @Singleton
    abstract fun bindCountryToCountryEntityMapper(
        impl: CountryToCountryEntityMapper
    ): Mapper<Country, CountryEntity>
}
