package com.example.todoappv2.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthViewModel

@Composable
fun ResetPasswordScreen(authViewModel: AuthViewModel,
                        onBackToLogin: () -> Unit
) {
    val state by authViewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Reset Password", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your registered email") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { authViewModel.onEvent(AuthEvent.ResetPassword(email)) }) {
            Text("Send Reset Link")
        }

        TextButton(onClick = onBackToLogin) { Text("Back to Login") }

        if (state.isLoading) CircularProgressIndicator()

    }
}