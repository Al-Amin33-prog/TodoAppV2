package com.example.todoappv2.auth.components.resetpassword


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.todoappv2.R

@Composable
fun ResetPasswordScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    val state = authViewModel.uiState.collectAsState().value
    
    ResetPasswordContent(
        state = state,
        onEvent = { authViewModel.onEvent(it) },
        onBackToLogin = onBackToLogin
    )
}

@Composable
fun ResetPasswordContent(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            shape = RoundedCornerShape(26.dp),
            value = email,
            onValueChange = {email = it},
            label = {Text("Email", style= MaterialTheme.typography.labelMedium)},
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {R.drawable.mail_24px},
            singleLine = true

        )
        
        Spacer(modifier = Modifier.height(32.dp))




        Button(
            onClick = { onEvent(AuthEvent.ResetPassword("test@email.com")) },
            modifier = Modifier.height(50.dp)
        ) {
            Text("Send Reset Link",style = MaterialTheme.typography.labelMedium)
        }

        TextButton(onClick = onBackToLogin) {
            Text("Back to Login")
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
fun ResetPasswordScreenPreview() {
    TodoAppV2Theme {
        ResetPasswordContent(
            state = AuthUiState(isLoading = false),
            onEvent = {},
            onBackToLogin = {}
        )
    }
}
