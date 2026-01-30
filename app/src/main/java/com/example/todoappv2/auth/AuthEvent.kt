package com.example.todoappv2.auth

sealed class AuthEvent {
    data class Login(
        val email: String,
        val password: String
    ):AuthEvent()
    data class Register(
        val name: String,
        val email: String,
        val password: String
    ): AuthEvent()
    object Logout: AuthEvent()
    data class ResetPassword(
        val email: String,

    ): AuthEvent()
}