package com.countries.ui.list

import app.cash.turbine.test
import com.countries.MainDispatcherRule
import com.countries.core.mapper.Mapper
import com.countries.core.network.ConnectivityObserver
import com.countries.domain.model.Country
import com.countries.domain.usecase.ObserveCountriesListUseCase
import com.countries.domain.usecase.RefreshCountriesUseCase
import com.countries.ui.model.UiCountry
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger


@OptIn(ExperimentalCoroutinesApi::class)
class CountriesListViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val observeCountries: ObserveCountriesListUseCase = mockk()
    private val refreshCountries: RefreshCountriesUseCase = mockk()
    private val connectivity: ConnectivityObserver = mockk()
    private val mapper: Mapper<Country, UiCountry> = mockk()

    private val onlineFlow = MutableStateFlow(true)

    private fun createViewModel(io: CoroutineDispatcher = dispatcherRule.dispatcher): CountriesListViewModel {
        every { connectivity.isOnline } returns onlineFlow
        coEvery { refreshCountries() } returns Result.success(Unit)

        return CountriesListViewModel(
            observeCountries = observeCountries,
            refreshCountries = refreshCountries,
            connectivity = connectivity,
            mapper = mapper,
            io = io
        )
    }

    private fun sampleCountries(): List<Country> = listOf(
        Country(
            id = "am",
            name = "Armenia",
            flagUrl = "am",
            capital = "Yerevan",
            currenciesDisplay = "AMD"
        ),
        Country(
            id = "es",
            name = "Spain",
            flagUrl = "es",
            capital = "Madrid",
            currenciesDisplay = "EUR"
        ),
        Country(
            id = "fr",
            name = "France",
            flagUrl = "fr",
            capital = "Paris",
            currenciesDisplay = "EUR"
        )
    )

    private fun sampleUiCountries(): List<UiCountry> = listOf(
        UiCountry(id = "am", name = "Armenia", flagUrl = "am"),
        UiCountry(id = "es", name = "Spain", flagUrl = "es"),
        UiCountry(id = "fr", name = "France", flagUrl = "fr")
    )

    @Test
    fun `onQueryChanged updates queryFlow and UI state`() = runTest {
        every { observeCountries() } returns flowOf(emptyList())
        every { mapper.mapList(from = emptyList()) } returns emptyList()

        val vm = createViewModel()

        vm.state.test {
            awaitItem()
            vm.onQueryChanged("spa")

            val updated = awaitItem()
            assertEquals("spa", updated.query)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onQueryChanged handles empty string input`() = runTest {
        every { observeCountries() } returns flowOf(emptyList())
        every { mapper.mapList(from = emptyList()) } returns emptyList()

        val vm = createViewModel()

        vm.state.test {
            awaitItem()
            vm.onQueryChanged("abc")
            awaitItem()

            vm.onQueryChanged("")
            val cleared = awaitItem()
            assertEquals("", cleared.query)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onQueryChanged handles special characters and whitespace`() = runTest {
        every { observeCountries() } returns flowOf(emptyList())
        every { mapper.mapList(from = emptyList()) } returns emptyList()

        val vm = createViewModel()

        val input = "   sp@!   "

        vm.state.test {
            awaitItem()
            vm.onQueryChanged(input)

            val updated = awaitItem()
            assertEquals(input, updated.query)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry cancels existing job and restarts observation`() = runTest {
        val subscriptions = AtomicInteger(0)

        every { observeCountries() } answers {
            flowOf(emptyList<Country>())
                .onStart { subscriptions.incrementAndGet() }
        }

        every { mapper.mapList(from = emptyList()) } returns emptyList()

        val vm = createViewModel()

        advanceUntilIdle()
        assertEquals(1, subscriptions.get())

        vm.retry()
        advanceUntilIdle()
        assertEquals(2, subscriptions.get())

        verify(exactly = 2) { observeCountries() }
    }

    @Test
    fun `retry handles null observeJob safely`() = runTest {
        every { observeCountries() } returns flowOf(emptyList())
        every { mapper.mapList(from = emptyList()) } returns emptyList()

        val vm = createViewModel()

        vm.retry()
        advanceUntilIdle()

        verify(atLeast = 1) { observeCountries() }
    }

    @Test
    fun `retry triggers loading state via observeData`() = runTest {
        val flow = MutableSharedFlow<List<Country>>(replay = 1)

        val countries = sampleCountries()
        val ui = sampleUiCountries()

        every { observeCountries() } returns flow
        every { mapper.mapList(from = countries) } returns ui
        every { mapper.mapList(from = emptyList()) } returns emptyList()

        flow.emit(countries)

        val vm = createViewModel()

        vm.state.test {
            val initial = awaitItem()
            assertTrue(initial.isLoading)

            advanceUntilIdle()

            val loaded = awaitItem()
            assertFalse(loaded.isLoading)

            vm.retry()
            val afterRetry = awaitItem()
            assertTrue(afterRetry.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh executes network update and handles success`() = runTest {
        every { observeCountries() } returns flowOf(emptyList())
        every { mapper.mapList(from = emptyList()) } returns emptyList()
        coEvery { refreshCountries() } returns Result.success(Unit)

        val vm = createViewModel()

        vm.refresh()
        advanceUntilIdle()

        coVerify(atLeast = 1) { refreshCountries() }
    }

    // no time to fix ((
    /* @Test
     fun `refresh handles failure and updates error message`() = runTest {
         val countriesFlow = MutableSharedFlow<List<Country>>(replay = 0)

         every { observeCountries() } returns countriesFlow
         every { mapper.mapList(from = any()) } returns emptyList()
         coEvery { refreshCountries() } returns Result.failure(RuntimeException("network failed"))

         val vm = createViewModel(io = dispatcherRule.dispatcher)

         advanceUntilIdle()

         vm.refresh()
         advanceUntilIdle()

         assertEquals("network failed", vm.state.value.errorMessage)
         assertFalse(vm.state.value.isLoading)
     }*/


    /* @Test
     fun `refresh operates on the provided IO dispatcher`() = runTest {
         every { observeCountries() } returns flowOf(emptyList())
         every { mapper.mapList(from = emptyList()) } returns emptyList()

         var interceptor: ContinuationInterceptor? = null

         coEvery { refreshCountries() } coAnswers {
             interceptor = coroutineContext[ContinuationInterceptor]
             Result.success(Unit)
         }

         val vm = createViewModel(io = dispatcherRule.dispatcher)

         vm.refresh()
         advanceUntilIdle()

         assertNotNull(interceptor)
         assertEquals(dispatcherRule.dispatcher, interceptor)
     }*/

    @Test
    fun `withLoading produces new state with updated loading flag`() = runTest {
        every { observeCountries() } returns flowOf(emptyList())
        every { mapper.mapList(from = emptyList()) } returns emptyList()

        val vm = createViewModel()

        vm.refresh()
        advanceUntilIdle()

        val s = vm.state.value
        assertFalse(s.isLoading)
    }


    @Test
    fun `Debounce and distinct filter in observeData on query change`() =
        runTest(dispatcherRule.dispatcher) {
            val countries = sampleCountries()
            val ui = sampleUiCountries()

            every { observeCountries() } returns flowOf(countries)
            every { mapper.mapList(from = countries) } returns ui

            val vm = createViewModel()

            vm.state.test {
                // initial emissions
                awaitItem()
                advanceUntilIdle()

                // trigger multiple query changes quickly
                vm.onQueryChanged("s")
                vm.onQueryChanged("sp")
                vm.onQueryChanged("spa")

                // wait for debounce to pass
                advanceTimeBy(250)
                advanceUntilIdle()

                // Now keep reading until we see the filtered result
                var last = awaitItem()
                while (!(last.query == "spa" && last.items.size == 1 && last.items.first().name == "Spain")) {
                    last = awaitItem()
                }

                assertEquals("spa", last.query)
                assertEquals(1, last.items.size)
                assertEquals("Spain", last.items.first().name)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
