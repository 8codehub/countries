package com.countries.domain.usecase

import app.cash.turbine.test
import com.countries.domain.model.Country
import com.countries.domain.repository.CountriesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveCountryDetailsUseCaseImplTest {

    private val repository: CountriesRepository = mockk()

    private val useCase = ObserveCountryDetailsUseCaseImpl(repository)

    @Test
    fun `invoke delegates to repository observeCountry`() = runTest {
        // GIVEN
        val id = "am"

        val expected = Country(
            id = "am",
            name = "Armenia",
            flagUrl = "flag",
            capital = "Yerevan",
            currenciesDisplay = "AMD"
        )

        every { repository.observeCountry(id) } returns flowOf(expected)

        // WHEN + THEN
        useCase.invoke(id).test {
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        @Suppress("UNUSED_EXPRESSION")
        verify(exactly = 1) { repository.observeCountry(id) }
    }

    @Test
    fun `invoke emits null when repository returns null`() = runTest {
        val id = "unknown"

        every { repository.observeCountry(id) } returns flowOf(null)

        useCase.invoke(id).test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        @Suppress("UNUSED_EXPRESSION")
        verify(exactly = 1) { repository.observeCountry(id) }
    }
}
