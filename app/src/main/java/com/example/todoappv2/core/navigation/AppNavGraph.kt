package com.example.todoappv2.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthGateScreen
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.auth.components.LoginScreen
import com.example.todoappv2.auth.components.RegisterScreen
import com.example.todoappv2.auth.components.ResetPasswordScreen
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    repository: AppRepository,
    schedule: TaskReminderSchedule,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GATE
    ){
        composable(Routes.AUTH_GATE){
            AuthGateScreen(
                authViewModel = authViewModel,
                onAuthenticated = {
                    navController.navigate(Routes.APP_SHEL){
                        popUpTo(Routes.AUTH_GATE){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onUnAuthenticated = {
                    navController.navigate(Routes.LOGIN){
                        popUpTo(Routes.AUTH_GATE){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(Routes.REGISTER){
            RegisterScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        // Headless route for logout action
        composable(Routes.LOGOUT){
            LaunchedEffect(Unit) {
                authViewModel.onEvent(AuthEvent.Logout)
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
        composable(Routes.RESET_PASSWORD){
            ResetPasswordScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.APP_SHEL) {
           AppNavigationShell(
               navController = navController,
               repository = repository,
               schedule = schedule,
               isDarkMode = isDarkMode,
               onThemeChange = onThemeChange
           )
        }
    }
}
