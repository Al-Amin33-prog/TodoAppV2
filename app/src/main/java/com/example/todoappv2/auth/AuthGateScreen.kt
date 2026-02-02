package com.example.todoappv2.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.todoappv2.core.navigation.Routes

@Composable
fun AuthGateScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController
){
   val state = authViewModel.uiState.collectAsState().value
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn){
            navController.navigate(Routes.SUBJECTS){
                popUpTo(Routes.AUTH_GATE){
                    inclusive = true
                }
            }
        }else{
            navController.navigate(Routes.LOGIN){
                popUpTo(Routes.AUTH_GATE){
                    inclusive = true
                }
            }
        }
    }
}