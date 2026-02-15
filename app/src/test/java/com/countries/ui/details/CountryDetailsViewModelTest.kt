package com.countries.ui.details

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountryDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val observeCountryDetails: ObserveCountryDetailsUseCase = mockk()
    private val mapper: Mapper<Country, UiCountryDetail> = mockk()

    private fun createViewModel(countryId: String?): CountryDetailsViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("id" to countryId))

        return CountryDetailsViewModel(
            savedStateHandle = savedStateHandle,
            observeCountryDetails = observeCountryDetails,
            mapper = mapper
        )
    }

    private fun sampleCountry() = Country(
        id = "es",
        name = "Spain",
        flagUrl = "es",
        capital = "Madrid",
        currenciesDisplay = "EUR"
    )

    private fun sampleUiCountry() = UiCountryDetail(
        countryFlag = "es",
        countryName = "Spain",
        capitalName = "Madrid",
        countryCurrencies = "EUR"
    )

    @Test
    fun `init calls observeCountryDetails`() = runTest {
        every { observeCountryDetails(id = "es") } returns flowOf(null)

        createViewModel("es")
        advanceUntilIdle()

        verify(exactly = 1) { observeCountryDetails(id = "es") }
    }

    @Test
    fun `init sets loading then emits mapped ui`() = runTest {
        val country = sampleCountry()
        val ui = sampleUiCountry()

        every { observeCountryDetails(id = "es") } returns flow {
            emit(country)
        }
        every { mapper.map(country) } returns ui

        val vm = createViewModel("es")

        vm.state.test {
            var state = awaitItem()

            // maybe bad solution, didn't find better one yet,
            // I think while is not the best one
            while (
                state.isLoading ||
                state.errorMessage != null ||
                state.uiCountryDetail.countryName == null
            ) {
                state = awaitItem()
            }

            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            assertEquals(ui, state.uiCountryDetail)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `init with null id sets error`() = runTest {
        val vm = createViewModel(null)

        advanceUntilIdle()

        assertEquals("Can’t load country details", vm.state.value.errorMessage)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `init handles upstream error`() = runTest {
        every { observeCountryDetails(id = "es") } returns flow {
            throw RuntimeException("boom")
        }

        val vm = createViewModel("es")
        advanceUntilIdle()

        assertEquals("boom", vm.state.value.errorMessage)
        assertFalse(vm.state.value.isLoading)
    }

    @Test
    fun `init clears error when valid country arrives`() = runTest {
        val upstream = MutableSharedFlow<Country?>(replay = 1)
        val country = sampleCountry()
        val ui = sampleUiCountry()

        every { observeCountryDetails(id = "xx") } returns flow {
            throw RuntimeException("network failed")
        }
        every { observeCountryDetails(id = "es") } returns upstream
        every { mapper.map(country) } returns ui

        createViewModel("xx")
        advanceUntilIdle()
        val vm = createViewModel("es")
        upstream.emit(country)
        advanceUntilIdle()

        assertNull(vm.state.value.errorMessage)
        assertEquals(ui, vm.state.value.uiCountryDetail)
    }
}
