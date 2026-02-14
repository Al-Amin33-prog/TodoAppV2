package com.example.todoappv2.auth.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Email", style= MaterialTheme.typography.labelMedium)},
            modifier = Modifier.fillMaxWidth()


        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text("Password", style= MaterialTheme.typography.labelMedium)},
            modifier = Modifier.fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onEvent(AuthEvent.Login("test@email.com", "12345"))
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Text("Login",style = MaterialTheme.typography.labelMedium)
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register",style = MaterialTheme.typography.bodyMedium )
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
