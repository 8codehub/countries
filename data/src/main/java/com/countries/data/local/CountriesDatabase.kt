package com.countries.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.countries.data.local.dao.CountryDao
import com.countries.data.local.entity.CountryEntity

@Database(
    entities = [CountryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CountriesDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
}