package com.example.todoappv2.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun AuthGateScreen(
    authViewModel: AuthViewModel,
   onAuthenticated: () -> Unit,
    onUnAuthenticated: () -> Unit
){
   val state = authViewModel.uiState.collectAsState().value
    LaunchedEffect(state.isLoggedIn, state.isEmailVerified) {
      if (state.isLoggedIn &&  state.isEmailVerified) onAuthenticated()
      else onUnAuthenticated()
    }
}