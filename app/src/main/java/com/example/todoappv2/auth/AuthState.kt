package com.example.todoappv2.auth

data class AuthState (
    val email: String ="",
    val password: String,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false ,
    val error: String? = null
)