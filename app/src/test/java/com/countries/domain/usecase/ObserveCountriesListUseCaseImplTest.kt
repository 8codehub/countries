package com.countries.domain.usecase

import app.cash.turbine.test
import com.countries.domain.model.Country
import com.countries.domain.repository.CountriesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ObserveCountriesListUseCaseTest {

    private lateinit var repository: CountriesRepository
    private lateinit var useCase: ObserveCountriesListUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveCountriesListUseCaseImpl(repository)
    }

    @Test
    fun `invoke delegates to repository observeCountries`() = runTest {
        every { repository.observeCountries() } returns flowOf(emptyList())

        useCase.invoke().test {
            assertEquals(emptyList<Country>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { repository.observeCountries() }
    }

    @Test
    fun `invoke emits countries from repository`() = runTest {
        val list1 = listOf(
            Country(
                id = "arm",
                name = "Armenia",
                capital = "Yerevan",
                flagUrl = "flag1",
                currenciesDisplay = "AMD"
            )
        )
        val list2 = listOf(
            Country(
                id = "esp",
                name = "Spain",
                capital = "Madrid",
                flagUrl = "flag2",
                currenciesDisplay = "EUR",
            )
        )

        every { repository.observeCountries() } returns flowOf(list1, list2)

        useCase.invoke().test {
            assertEquals(list1, awaitItem())
            assertEquals(list2, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { repository.observeCountries() }
    }

    @Test
    fun `invoke propagates repository flow error`() = runTest {
        val error = IllegalStateException("boom")

        every { repository.observeCountries() } returns failingFlow(error)

        useCase.invoke().test {
            val thrown = awaitError()
            assertEquals("boom", thrown.message)
        }

        verify(exactly = 1) { repository.observeCountries() }
    }

    private fun <T> failingFlow(t: Throwable): Flow<T> = flow {
        throw t
    }
}
