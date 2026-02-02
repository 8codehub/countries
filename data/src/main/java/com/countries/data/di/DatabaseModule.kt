package com.countries.data.di


import android.content.Context
import androidx.room.Room
import com.countries.data.local.CountriesDatabase
import com.countries.data.local.dao.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CountriesDatabase {
        return Room.databaseBuilder(
            context,
            CountriesDatabase::class.java,
            "countries.db"
        ).build()
    }

    @Provides
    fun provideCountryDao(db: CountriesDatabase): CountryDao =
        db.countryDao()
}
