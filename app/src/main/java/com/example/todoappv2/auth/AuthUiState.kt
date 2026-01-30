package com.example.todoappv2.auth

import com.google.firebase.auth.FirebaseUser


data class AuthUiState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null,
    val isLoggedIn: Boolean = false

)