package com.countries.ui.state

import com.countries.core.arch.BaseUiState
import com.countries.ui.model.UiCountryDetail

data class CountryDetailsUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val uiCountryDetail: UiCountryDetail = UiCountryDetail()
) : BaseUiState