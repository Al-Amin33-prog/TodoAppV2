package com.example.todoappv2.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthUiState
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.ui.theme.TodoAppV2Theme

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit
){
    val state = authViewModel.uiState.collectAsState().value
    
    LoginContent(
        state = state,
        onEvent = { authViewModel.onEvent(it) },
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginContent(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder for TextFields (Email/Password)
        Text("Email and Password fields will go here")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onEvent(AuthEvent.Login("test@email.com", "12345"))
            },
            modifier = Modifier.height(50.dp)
        ) {
            Text("Login")
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TodoAppV2Theme {
        LoginContent(
            state = AuthUiState(isLoading = false),
            onEvent = {},
            onNavigateToRegister = {}
        )
    }
}
