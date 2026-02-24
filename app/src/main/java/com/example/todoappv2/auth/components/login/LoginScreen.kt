package com.example.todoappv2.auth.components.login



import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.tooling.preview.Preview

import com.example.todoappv2.auth.AuthUiState
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.ui.theme.TodoAppV2Theme

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit
){
    val state = authViewModel.uiState.collectAsState().value

    LoginContent(
        state = state,
        onEvent = { authViewModel.onEvent(it) },
        onNavigateToRegister = onNavigateToRegister,
        onLoginSuccess = onLoginSuccess,
        onForgotPassword = onForgotPassword
    )
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TodoAppV2Theme {
        LoginContent(
            state = AuthUiState(isLoading = false),
            onEvent = {},
            onNavigateToRegister = {},
            onLoginSuccess = {},
            onForgotPassword = {}
        )
    }
}
