package com.countries.core.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MVVMViewModel<S : BaseUiState>(
    initialState: S
) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected fun updateState(reducer: (S) -> S) {
        _state.update(reducer)
    }

    protected fun setLoading(isLoading: Boolean = true) {
        updateState { it.withLoading(isLoading).withError(null) }
    }

    protected fun clearError() {
        updateState { it.withError(null) }
    }

    protected fun setError(message: String?) {
        updateState { it.withLoading(false).withError(message) }
    }

    protected fun launchOn(
        dispatcher: CoroutineDispatcher,
        onError: (Throwable) -> Unit = { setError(it.message) },
        block: suspend () -> Unit
    ): Job {
        val handler = CoroutineExceptionHandler { _, t -> onError(t) }
        return viewModelScope.launch(dispatcher + handler) { block() }
    }

    protected abstract fun S.withLoading(value: Boolean): S
    protected abstract fun S.withError(value: String?): S
}
