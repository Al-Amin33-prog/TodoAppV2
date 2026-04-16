package com.example.todoappv2.auth

import com.example.todoappv2.domain.UserModel



data class AuthUiState(
    val isLoading: Boolean = false,
    val user: UserModel? = null,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val isLoggedOut: Boolean = false,
    val isEmailVerified: Boolean = false,
    val emailVerificationSent: Boolean = false

)