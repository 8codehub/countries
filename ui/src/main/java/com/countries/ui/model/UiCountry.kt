package com.countries.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiCountry(
    val id: String,
    val name: String,
    val flagUrl: String? = null
)