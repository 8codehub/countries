package com.countries.ui.details

import app.cash.turbine.test
import com.countries.MainDispatcherRule
import com.countries.core.mapper.Mapper
import com.countries.domain.model.Country
import com.countries.domain.usecase.ObserveCountryDetailsUseCase
import com.countries.ui.model.UiCountryDetail
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalCoroutinesApi::class)
class CountryDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val observeCountryDetails: ObserveCountryDetailsUseCase = mockk()
    private val mapper: Mapper<Country, UiCountryDetail> = mockk()

    private fun createViewModel(): CountryDetailsViewModel =
        CountryDetailsViewModel(
            observeCountryDetails = observeCountryDetails,
            mapper = mapper
        )

    private fun defaultUiDetail(): UiCountryDetail = UiCountryDetail(
        countryFlag = null,
        countryName = null,
        capitalName = null,
        countryCurrencies = null
    )

    private fun sampleCountry(): Country =
        Country(
            id = "es",
            name = "Spain",
            flagUrl = "es",
            capital = "Madrid",
            currenciesDisplay = "EUR"
        )

    private fun sampleUiCountryDetail(): UiCountryDetail =
        UiCountryDetail(
            countryFlag = "es",
            countryName = "Spain",
            capitalName = "Madrid",
            countryCurrencies = "EUR"
        )

    @Test
    fun `load calls observeCountryDetails with id`() = runTest {
        every { observeCountryDetails(id = "es") } returns flowOf(null)

        val vm = createViewModel()
        vm.load("es")
        advanceUntilIdle()

        verify(exactly = 1) { observeCountryDetails(id = "es") }
    }

    @Test
    fun `load sets loading true onStart then emits mapped ui on country`() = runTest {
        val upstream = MutableSharedFlow<Country?>(replay = 0)
        val country = sampleCountry()
        val ui = sampleUiCountryDetail()

        every { observeCountryDetails(id = "es") } returns upstream
        every { mapper.map(country) } returns ui

        val vm = createViewModel()

        vm.state.test {
            val initial = awaitItem()
            assertFalse(initial.isLoading)
            assertNull(initial.errorMessage)
            assertEquals(defaultUiDetail(), initial.uiCountryDetail)

            vm.load("es")

            val loading = awaitItem()
            assertTrue(loading.isLoading)

            assertEquals(defaultUiDetail(), loading.uiCountryDetail)

            upstream.emit(country)
            advanceUntilIdle()

            val loaded = awaitItem()
            assertFalse(loaded.isLoading)
            assertNull(loaded.errorMessage)
            assertEquals(ui, loaded.uiCountryDetail)

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) { mapper.map(country) }
    }

    @Test
    fun `load handles null country by setting loading false and clearing error`() = runTest {
        val upstream = MutableSharedFlow<Country?>(replay = 0)
        every { observeCountryDetails(id = "es") } returns upstream

        val vm = createViewModel()

        vm.state.test {
            val initial = awaitItem()
            assertEquals(defaultUiDetail(), initial.uiCountryDetail)

            vm.load("es")
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            upstream.emit(null)
            advanceUntilIdle()

            val afterNull = awaitItem()
            assertFalse(afterNull.isLoading)
            assertNull(afterNull.errorMessage)


            assertEquals(defaultUiDetail(), afterNull.uiCountryDetail)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load handles upstream error by setting errorMessage`() = runTest {
        every { observeCountryDetails(id = "es") } returns flow {
            throw RuntimeException("boom")
        }

        val vm = createViewModel()

        vm.load("es")
        advanceUntilIdle()

        assertEquals("boom", vm.state.value.errorMessage)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `load cancels previous observation and restarts collecting`() = runTest {
        val subscriptions = AtomicInteger(0)

        val firstFlow = flowOf<Country?>(null).onStart { subscriptions.incrementAndGet() }
        val secondFlow = flowOf<Country?>(null).onStart { subscriptions.incrementAndGet() }

        every { observeCountryDetails(id = "es") } returns firstFlow
        every { observeCountryDetails(id = "fr") } returns secondFlow

        val vm = createViewModel()

        vm.load("es")
        advanceUntilIdle()
        assertEquals(1, subscriptions.get())

        vm.load("fr")
        advanceUntilIdle()
        assertEquals(2, subscriptions.get())

        verify(exactly = 1) { observeCountryDetails(id = "es") }
        verify(exactly = 1) { observeCountryDetails(id = "fr") }
    }

    @Test
    fun `load clears previous error when a valid country arrives`() = runTest {
        val upstream = MutableSharedFlow<Country?>(replay = 0)
        val country = sampleCountry()
        val ui = sampleUiCountryDetail()

        every { observeCountryDetails(id = "xx") } returns flow {
            throw RuntimeException("network failed")
        }

        every { observeCountryDetails(id = "es") } returns upstream
        every { mapper.map(country) } returns ui

        val vm = createViewModel()

        vm.load("xx")
        advanceUntilIdle()
        assertEquals("network failed", vm.state.value.errorMessage)

        vm.load("es")
        advanceUntilIdle()

        upstream.emit(country)
        advanceUntilIdle()

        assertNull(vm.state.value.errorMessage)
        assertEquals(ui, vm.state.value.uiCountryDetail)
    }
}
