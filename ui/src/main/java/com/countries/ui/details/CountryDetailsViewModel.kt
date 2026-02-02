package com.countries.ui.details

import androidx.lifecycle.viewModelScope
import com.countries.core.arch.MVVMViewModel
import com.countries.core.mapper.Mapper
import com.countries.domain.model.Country
import com.countries.domain.usecase.ObserveCountryDetailsUseCase
import com.countries.ui.model.UiCountryDetail
import com.countries.ui.state.CountryDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    private val observeCountryDetails: ObserveCountryDetailsUseCase,
    private val mapper: Mapper<Country, UiCountryDetail>
) : MVVMViewModel<CountryDetailsUiState>(
    initialState = CountryDetailsUiState()
) {

    private var observeJob: Job? = null

    fun load(countryId: String) {
        restart(countryId)
    }

    private fun restart(countryId: String) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeCountryDetails(id = countryId)
                .onStart { setLoading(true) }
                .catch { setError(it.message) }
                .collect { country -> onCountry(country) }
        }
    }

    private fun onCountry(country: Country?) {
        if (country == null) {
            setLoading(false)
            clearError()
            return
        }

        val ui = mapper.map(country)
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = null,
                uiCountryDetail = ui
            )
        }
    }

    override fun CountryDetailsUiState.withLoading(value: Boolean) = copy(isLoading = value)
    override fun CountryDetailsUiState.withError(value: String?) = copy(errorMessage = value)
}
