package com.example.testapp.user.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Result
import com.example.testapp.common.asResult
import com.example.testapp.user.domain.usecase.UsersUseCase
import com.example.testapp.user.presentation.model.UserListUiState
import com.example.testapp.user.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class UsersListViewModel @Inject constructor(
    private val getUsersUseCase: UsersUseCase
) : BaseViewModel() {

    private val _isFavoriteUsersEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state: Flow<UserListUiState> = combine(
        _errorStateMessage,
        _isFavoriteUsersEnabled,
        _isRefreshing,
        _isFavoriteUsersEnabled.flatMapLatest { isEnabled ->
            getUsersUseCase.getUsersAsFlow(isEnabled)
                .map { users -> users.map { it.toUiModel() } }
                .asResult()
        }
    ) { errorStateMessage, isFavoriteUsersEnabled, isRefreshing, result ->
        when (result) {
            is Result.Loading -> {
                UserListUiState(
                    isLoading = true,
                    isRefreshing = isRefreshing,
                    isFavoriteUsersEnabled = isFavoriteUsersEnabled
                )
            }

            is Result.Success -> {
                UserListUiState(
                    users = result.data,
                    isRefreshing = isRefreshing,
                    isFavoriteUsersEnabled = isFavoriteUsersEnabled,
                    remoteErrorMessage = errorStateMessage.errorMessage
                )
            }

            is Result.Error -> {
                UserListUiState(
                    isRefreshing = isRefreshing,
                    isFavoriteUsersEnabled = isFavoriteUsersEnabled,
                    errorMessage = result.exception?.message
                )
            }
        }
    }

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserListUiState()
    )

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        _isRefreshing.value = true
        executeErrorCoroutine(errorBlock = {
            _isRefreshing.value = false
        }) {
            delay(REQUEST_DELAY)
            getUsersUseCase.fetchUsers(10)
            _isRefreshing.value = false
        }
    }

    fun onFavoriteClick(id: Long, isFavorite: Boolean) {
        executeCoroutine {
            getUsersUseCase.updateIsFavoriteById(id, isFavorite)
        }
    }

    fun switchUserFilter(isFavoriteUsersEnabled: Boolean) {
        _isFavoriteUsersEnabled.update { isFavoriteUsersEnabled }
    }

    companion object {
        private const val REQUEST_DELAY = 300L
    }
}