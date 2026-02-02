package com.countries.data.di

import com.countries.core.mapper.Mapper
import com.countries.data.local.entity.CountryEntity
import com.countries.data.mapper.CountryDtoToCountryEntityMapper
import com.countries.data.mapper.CountryEntityToCountryMapper
import com.countries.data.mapper.CountryToCountryEntityMapper
import com.countries.data.remote.dto.CountryDto
import com.countries.domain.model.Country
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MapperModule {
    @Provides
    @Singleton
    fun provideCountryDtoToCountryEntityMapper(): Mapper<CountryDto, CountryEntity> =
        CountryDtoToCountryEntityMapper()

    @Provides
    @Singleton
    fun provideCountryEntityToCountryMapper(): Mapper<CountryEntity, Country> =
        CountryEntityToCountryMapper()


    @Provides
    @Singleton
    fun provideCountryToCountryEntityMapper(): Mapper<Country, CountryEntity> =
        CountryToCountryEntityMapper()
}
