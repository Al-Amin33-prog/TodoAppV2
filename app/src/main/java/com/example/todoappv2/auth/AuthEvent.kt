package com.example.todoappv2.auth

sealed class AuthEvent {
    data class EmailChanged(val email: String): AuthEvent()
    data class PassWordChanged(val password: String): AuthEvent()
    data class ConFirmPasswordChanged(val password: String): AuthEvent()

    object Login: AuthEvent()
    object Register: AuthEvent()
    object Logout: AuthEvent()
}