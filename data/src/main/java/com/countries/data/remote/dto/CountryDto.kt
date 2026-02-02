package com.countries.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CountryDto(
    @param:Json(name = "name")
    val name: NameDto,

    @param:Json(name = "cca3")
    val cca3: String,

    @param:Json(name = "currencies")
    val currencies: Map<String, CurrencyDto> = emptyMap(),

    @param:Json(name = "capital")
    val capital: List<String> = emptyList(),

    @param:Json(name = "flags")
    val flags: FlagsDto? = null,

    @param:Json(name = "flag")
    val flagEmoji: String? = null
)

@JsonClass(generateAdapter = true)
internal data class FlagsDto(
    @Json(name = "png")
    val png: String? = null,

    @Json(name = "svg")
    val svg: String? = null,

    @Json(name = "alt")
    val alt: String? = null
)

@JsonClass(generateAdapter = true)
internal data class NameDto(
    @Json(name = "common")
    val common: String,

    @Json(name = "official")
    val official: String,

    @Json(name = "nativeName")
    val nativeName: Map<String, NativeNameDto> = emptyMap()
)

@JsonClass(generateAdapter = true)
internal data class NativeNameDto(
    @Json(name = "official")
    val official: String,

    @Json(name = "common")
    val common: String
)

@JsonClass(generateAdapter = true)
internal data class CurrencyDto(
    @Json(name = "name")
    val name: String? = null,

    @Json(name = "symbol")
    val symbol: String? = null
)