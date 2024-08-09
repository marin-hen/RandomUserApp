package com.example.testapp.user.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.R
import com.example.testapp.user.domain.exception.NoInternetException
import com.example.testapp.user.presentation.model.ErrorUiMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _errorStateMessage: MutableStateFlow<ErrorUiMessage> =
        MutableStateFlow(ErrorUiMessage(null))
    val errorStateMessage: StateFlow<ErrorUiMessage> = _errorStateMessage.asStateFlow()

    private fun errorHandler(caughtError: (err: Throwable) -> Unit) =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            caughtError.invoke(throwable)
            _errorStateMessage.value = ErrorUiMessage(throwable.toErrorUiMessage())
        }

    fun executeCoroutine(
        errorBlock: (Throwable) -> Unit = {},
        block: suspend () -> Unit,
    ): Job {
        val handler = errorHandler(errorBlock)
        return viewModelScope.launch(handler) { block.invoke() }
    }

    fun executeErrorCoroutine(
        errorBlock: (Throwable) -> Unit = {},
        block: suspend () -> Unit,
    ): Job {
        val handler = errorHandler(errorBlock)
        return viewModelScope.launch(handler) {
            onClearErrorState()
            block.invoke()
        }
    }

    fun onClearErrorState() {
        _errorStateMessage.value = ErrorUiMessage(null)
    }

    private fun Throwable.toErrorUiMessage(): Int {
        return when (this) {
            is NoInternetException -> R.string.error_no_internet_connection
            else -> R.string.error_general
        }
    }
}