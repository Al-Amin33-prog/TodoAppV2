package com.example.todoappv2.auth.components.register


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.tooling.preview.Preview

import com.example.todoappv2.auth.AuthUiState
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.ui.theme.TodoAppV2Theme

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit,

){
    val state by authViewModel.uiState.collectAsState()

    RegisterContent(
        state = state,
        onEvent = { authViewModel.onEvent(it) },
        onBackToLogin = onBackToLogin,
        onRegisterSuccess = onBackToLogin

    )
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    TodoAppV2Theme {
        RegisterContent(
            state = AuthUiState(),
            onEvent = {},
            onBackToLogin = {},
            onRegisterSuccess = {}
        )
    }
}
