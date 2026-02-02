package com.countries.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey
    val cca3: String,

    val name: String,

    val capital: String,

    val flag: String,

    val currencies: String
)