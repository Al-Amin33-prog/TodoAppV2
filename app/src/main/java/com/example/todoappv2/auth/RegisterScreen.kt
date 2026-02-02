package com.example.todoappv2.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit
){
    Column{
        Button(onClick = {
            authViewModel.onEvent(
                AuthEvent.Register("new@email.com","User","12345")
            )
        }) {
            Text("Register")
        }
        TextButton(onClick = onBackToLogin) {
            Text("Back To Login")
        }
    }
}