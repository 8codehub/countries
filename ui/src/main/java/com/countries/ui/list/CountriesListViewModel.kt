package com.countries.ui.list

import com.countries.core.arch.MVVMViewModel
import com.countries.core.dispatcher.IoDispatcher
import com.countries.core.mapper.Mapper
import com.countries.core.network.ConnectivityObserver
import com.countries.domain.model.Country
import com.countries.domain.usecase.ObserveCountriesListUseCase
import com.countries.domain.usecase.RefreshCountriesUseCase
import com.countries.ui.model.UiCountry
import com.countries.ui.state.CountriesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CountriesListViewModel @Inject constructor(
    private val observeCountries: ObserveCountriesListUseCase,
    private val refreshCountries: RefreshCountriesUseCase,
    private val connectivity: ConnectivityObserver,
    private val mapper: Mapper<Country, UiCountry>,
    @param:IoDispatcher private val io: CoroutineDispatcher
) : MVVMViewModel<CountriesListUiState>(
    initialState = CountriesListUiState(isLoading = true)
) {

    private val queryFlow = MutableStateFlow("")
    private var observeJob: Job? = null

    init {
        start()
    }

    fun onQueryChanged(query: String) {
        queryFlow.value = query
        updateState { it.copy(query = query) }
    }

    fun retry() {
        observeJob?.cancel()
        observeJob = null
        observeData()
    }

    fun refresh() {
        launchOn(io) {
            setLoading(true)
            refreshCountries().onFailure { setError(it.message) }
        }
    }

    private fun start() {
        observeData()
        refresh()
        observeConnectivity()
    }

    @OptIn(FlowPreview::class)
    private fun observeData() {
        if (observeJob?.isActive == true) return

        observeJob = launchOn(io) {
            combine(
                countriesUiFlow(),
                queryFlow
                    .debounce(QUERY_DEBOUNCE_MS)
                    .distinctUntilChanged()
            ) { items, query ->
                items.filteredBy(query)
            }
                .onStart { setLoading(true) }
                .collect { items ->
                    updateState {
                        it.copy(
                            items = items,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    private fun observeConnectivity() {
        launchOn(io) {
            var wasOnline: Boolean? = null

            connectivity.isOnline.collect { online ->
                when {
                    online && wasOnline == false -> {
                        updateState { it.copy(bottomStatus = BottomStatus.Online) }
                        refresh()
                    }

                    !online -> updateState { it.copy(bottomStatus = BottomStatus.NoInternet) }
                }
                wasOnline = online
            }
        }
    }

    private fun countriesUiFlow() =
        observeCountries().map { mapper.mapList(from = it) }

    private fun List<UiCountry>.filteredBy(query: String): List<UiCountry> {
        val q = query.trim()
        if (q.isBlank()) return this
        return filter { it.name.contains(q, ignoreCase = true) }
    }

    override fun CountriesListUiState.withLoading(value: Boolean) = copy(isLoading = value)
    override fun CountriesListUiState.withError(value: String?) = copy(errorMessage = value)

    private companion object {
        const val QUERY_DEBOUNCE_MS = 250L
    }
}
