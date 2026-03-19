package com.example.todoappv2.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            repository.observeAuthState().collect { user ->
                _uiState.value = _uiState.value.copy(
                    user = user,
                    isLoggedIn = user!= null,
                    isLoading = false,
                    error = null

                )
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {

            is AuthEvent.Login -> {
                login(event.email, event.password)
            }

            is AuthEvent.Register -> {
                register(event.name, event.email, event.password)
            }

            AuthEvent.Logout -> {
                logout()
            }

            is AuthEvent.ResetPassword -> {
                resetPassword(event.email)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.login(email, password)

             result.fold(
                onSuccess = {

                },
                onFailure = { error ->
                    _uiState.value =  _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    private fun register(name: String,email: String,  password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.register(name, email, password)

             result.fold(
                onSuccess = {
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState()
        }
    }

    private fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.resetPassword(email)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, error = null)
                },
                onFailure = { error ->
                    _uiState.value.copy(isLoading = false, error = error.message)
                }
            )
        }
    }
}