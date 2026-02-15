package com.countries.ui.state

import androidx.compose.runtime.Immutable
import com.countries.core.arch.BaseUiState
import com.countries.ui.list.BottomStatus
import com.countries.ui.model.UiCountry

@Immutable
data class CountriesListUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val query: String = "",
    val items: List<UiCountry> = emptyList(),
    val bottomStatus: BottomStatus = BottomStatus.Online
) : BaseUiState