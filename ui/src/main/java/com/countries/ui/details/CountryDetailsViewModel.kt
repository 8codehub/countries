package com.countries.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.countries.core.arch.MVVMViewModel
import com.countries.core.mapper.Mapper
import com.countries.domain.model.Country
import com.countries.domain.usecase.ObserveCountryDetailsUseCase
import com.countries.ui.model.UiCountryDetail
import com.countries.ui.navigation.NavigationRoute
import com.countries.ui.state.CountryDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeCountryDetails: ObserveCountryDetailsUseCase,
    private val mapper: Mapper<Country, UiCountryDetail>
) : MVVMViewModel<CountryDetailsUiState>(
    initialState = CountryDetailsUiState()
) {

/*
    private val args = savedStateHandle.toRoute<NavigationRoute.CountryDetails>()
*/

    private val countryId: String? =
        savedStateHandle["id"]


    init {
        load(countryId = countryId)
    }

    private var observeJob: Job? = null

    private fun load(countryId: String?) {
        if (countryId.isNullOrBlank()) {
            /*Will talk about error cases and single events*/
            setError("Can’t load country details")
            return
        }
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeCountryDetails(id = countryId)
                .onStart { setLoading(true) }
                .catch { setError(it.message) }
                .collect { country -> onCountry(country) }
        }
    }


    private fun onCountry(country: Country?) {
        setLoading(false)
        clearError()
        if (country == null) {
            setError("Can’t load country details")
            return
        }
        val ui = mapper.map(country)
        updateState {
            it.copy(uiCountryDetail = ui)
        }
    }

    override fun CountryDetailsUiState.withLoading(value: Boolean) = copy(isLoading = value)
    override fun CountryDetailsUiState.withError(value: String?) = copy(errorMessage = value)
}
