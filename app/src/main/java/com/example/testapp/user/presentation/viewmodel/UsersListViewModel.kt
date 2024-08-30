package com.example.testapp.user.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.testapp.common.network.Result
import com.example.testapp.common.network.asResult
import com.example.testapp.user.domain.interactor.UsersInteractor
import com.example.testapp.user.presentation.model.UserListUiState
import com.example.testapp.user.presentation.model.UserUiModel
import com.example.testapp.user.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
@HiltViewModel
internal class UsersListViewModel @Inject constructor(
    private val getUsersInteractor: UsersInteractor
) : BaseViewModel() {

    private val _state: MutableStateFlow<UserListUiState> = MutableStateFlow(UserListUiState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserListUiState(isLoading = true)
    )

    init {
        fetchUsers()

        _state
            .map {
                it.isRefreshing
            }
            .distinctUntilChanged()
            .filter { it }
            .onEach {
                fetchUsers()
            }
            .launchIn(viewModelScope)

        _state
            .map {
                it.isFavoriteUsersEnabled
            }
            .distinctUntilChanged()
            .flatMapLatest { isEnabled ->
                getUsersInteractor.getUsersAsFlow(isEnabled)
                    .map { users -> users.map { it.toUiModel() } }
                    .asResult()
            }
            .onEach { processingUserData(it) }
            .launchIn(viewModelScope)
    }

    private fun processingUserData(value: Result<List<UserUiModel>>) {
        _state.update { lastState ->
            when (value) {
                is Result.Loading -> {
                    lastState.copy(isLoading = true)
                }

                is Result.Success -> lastState.copy(
                    users = value.data.toImmutableList(),
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = null
                )

                is Result.Error -> lastState.copy(
                    errorMessage = value.exception?.message,
                    isRefreshing = false,
                    isLoading = false
                )
            }
        }
    }

    fun refreshUsers() {
        _state.update { it.copy(isRefreshing = true) }
    }

    fun fetchUsers() {
        executeCoroutine(
            block = {
                getUsersInteractor.fetchUsers(10)
            }
        )
    }

    fun onFavoriteClick(id: Long, isFavorite: Boolean) {
        executeCoroutine {
            getUsersInteractor.updateIsFavoriteById(id, isFavorite)
        }
    }

    fun switchUserFilter(isFavoriteUsersEnabled: Boolean) {
        _state.update { it.copy(isFavoriteUsersEnabled = isFavoriteUsersEnabled) }
    }
}