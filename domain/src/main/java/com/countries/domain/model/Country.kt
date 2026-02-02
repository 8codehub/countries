package com.countries.domain.model

data class Country(
    val id: String,// cca3 (primary key)
    val name: String,
    val capital: String?,
    val currenciesDisplay: String,
    val flagUrl: String?
)