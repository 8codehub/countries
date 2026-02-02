package com.countries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.countries.data.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun observeCountries(): Flow<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Query("DELETE FROM countries")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM countries")
    suspend fun countCountries(): Int


    @Query(
        """
        SELECT * FROM countries
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name ASC
        """
    )
    suspend fun search(query: String): List<CountryEntity>

    @Query("SELECT * FROM countries WHERE cca3 = :id LIMIT 1")
    fun observeCountry(id: String): Flow<CountryEntity?>
}