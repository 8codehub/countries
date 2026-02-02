package com.countries.domain.di

import com.countries.domain.usecase.ObserveCountriesListUseCase
import com.countries.domain.usecase.ObserveCountriesListUseCaseImpl
import com.countries.domain.usecase.ObserveCountryDetailsUseCase
import com.countries.domain.usecase.ObserveCountryDetailsUseCaseImpl
import com.countries.domain.usecase.RefreshCountriesUseCase
import com.countries.domain.usecase.RefreshCountriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class UseCaseModule {

    @Binds
    abstract fun bindObserveCountryDetailsUseCase(
        implementation: ObserveCountryDetailsUseCaseImpl
    ): ObserveCountryDetailsUseCase


    @Binds
    abstract fun bindObserveCountriesListUseCase(
        implementation: ObserveCountriesListUseCaseImpl
    ): ObserveCountriesListUseCase


    @Binds
    abstract fun bindRefreshCountriesUseCase(
        implementation: RefreshCountriesUseCaseImpl
    ): RefreshCountriesUseCase
}