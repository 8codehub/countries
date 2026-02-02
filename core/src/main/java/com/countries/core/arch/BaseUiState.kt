package com.countries.core.arch

interface BaseUiState {
    val isLoading: Boolean
    val errorMessage: String?
}