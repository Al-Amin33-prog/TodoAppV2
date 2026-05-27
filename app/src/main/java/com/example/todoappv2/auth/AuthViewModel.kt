package com.example.todoappv2.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.core.session.SessionManager
import com.example.todoappv2.data.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            sessionManager.userFlow.collect { user ->
                _uiState.update { it.copy(
                    user = user,
                    isLoggedIn = user != null,
                    isEmailVerified = user?.isEmailVerified ?: false,
                    isLoading = false,
                    isCheckingAuth = false,
                    error = null
                )}
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(event.email, event.password)
            is AuthEvent.Register -> register(event.name, event.email, event.password)
            AuthEvent.Logout -> logout()
            is AuthEvent.ResetPassword -> resetPassword(event.email)
            AuthEvent.ResendVerificationEmail -> resendVerificationEmail()
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.login(email, password).fold(
                onSuccess = { user ->
                    // Saving the user will trigger the observeAuthState collection
                    sessionManager.saveUser(user)
                },
                onFailure = { error ->
                    val message = when(error) {
                        is FirebaseAuthInvalidCredentialsException -> "Incorrect password"
                        is FirebaseAuthInvalidUserException -> "User not found"
                        is FirebaseNetworkException -> "No internet connection"
                        else -> error.message ?: "Something went wrong"
                    }
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
            )
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.register(name, email, password).fold(
                onSuccess = { user ->
                    sessionManager.saveUser(user)
                    repository.sendEmailVerification()
                    repository.logout()
                    // Clear user from session as they need to verify first
                    sessionManager.clearUser() 
                    _uiState.update { it.copy(
                        isLoading = false,
                        emailVerificationSent = true
                    )}
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout()
            sessionManager.clearUser()
            _uiState.value = AuthUiState()
        }
    }

    private fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, passwordResetSent = false, error = null) }
            
            repository.resetPassword(email).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, passwordResetSent = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    private fun resendVerificationEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.sendEmailVerification().fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, emailVerificationSent = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
}
