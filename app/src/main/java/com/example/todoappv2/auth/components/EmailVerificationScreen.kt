package com.example.todoappv2.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthUiState
import com.example.todoappv2.auth.AuthViewModel

@Composable
fun EmailVerificationPendingScreen(
    authViewModel: AuthViewModel,
    onBackToLogin: () -> Unit
){
    val state = authViewModel.uiState.collectAsState().value
    EmailVerificationPendingContent(
      state = state,
        onResend = {authViewModel.onEvent(AuthEvent.ResendVerificationEmail)},
        onBackToLogin = onBackToLogin
    )
}
@Composable
fun EmailVerificationPendingContent(
    state: AuthUiState,
    onResend: () -> Unit,
    onBackToLogin: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
}