package com.countries.ui.di

import com.countries.core.mapper.Mapper
import com.countries.domain.model.Country
import com.countries.ui.mapper.CountryToUiCountryMapper
import com.countries.ui.mapper.CountryToUiUiCountryDetailMapper
import com.countries.ui.model.UiCountry
import com.countries.ui.model.UiCountryDetail
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    @Singleton
    fun provideCountryToUiCountryMapper(): Mapper<Country, UiCountry> =
        CountryToUiCountryMapper()

    @Provides
    @Singleton
    fun provideCountryToUiCountryDetailMapper(): Mapper<Country, UiCountryDetail> =
        CountryToUiUiCountryDetailMapper()
}