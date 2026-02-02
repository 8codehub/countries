package com.countries.data.remote

import com.countries.data.remote.dto.CountryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CountriesApi {

    @GET("v3.1/all")
    suspend fun getAll(
        @Query("fields") fields: String =
            "cca2,cca3,name,capital,currencies,flags"
    ): Response<List<CountryDto>>

    @GET("v3.1/name/{name}")
    suspend fun searchByName(
        @Path("name") name: String,
        @Query("fields") fields: String =
            "cca2,cca3,name,capital,currencies,flags"
    ): Response<List<CountryDto>>
}