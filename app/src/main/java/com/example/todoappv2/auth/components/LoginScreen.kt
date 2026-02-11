package com.example.todoappv2.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit
){
    val state = authViewModel.uiState.collectAsState().value
    Column {
        Button(onClick = {
            authViewModel.onEvent(
                AuthEvent.Login("test@enail.com","12345")
            )
        }) {
            Text("Login")
        }
        TextButton(onClick = onNavigateToRegister) {
            Text("Register")
        }
        if (state.isLoading) CircularProgressIndicator()
        state.error?.let { Text(it) }
    }
}