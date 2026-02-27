package com.example.todoappv2.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthGateScreen
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.auth.components.login.LoginContent
import com.example.todoappv2.auth.components.login.LoginScreen
import com.example.todoappv2.auth.components.register.RegisterContent
import com.example.todoappv2.auth.components.register.RegisterScreen
import com.example.todoappv2.auth.components.resetpassword.ResetPasswordScreen
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.settings.SettingsScreen


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
                    navController.navigate(Routes.APP_SHELL){
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
                },

                onLoginSuccess = {
                    navController.navigate(Routes.APP_SHELL){
                        popUpTo(Routes.LOGIN ){inclusive = true}
                    }
                },
                onForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }
        composable("forgot_password"){
            ResetPasswordScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                    navController.popBackStack()
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
                    popUpTo(Routes.AUTH_GATE) { inclusive = true }
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


        composable(Routes.APP_SHELL) {
           AppNavigationShell(
               navController = navController,
               repository = repository,
               schedule = schedule,
               isDarkMode = isDarkMode,
               onThemeChange = onThemeChange
           )
        }
        composable(Routes.SETTINGS){
            SettingsScreen(
                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onLogout = {
                    navController.navigate(Routes.LOGOUT)
                },
                onBack = {
                    navController.popBackStack()
                },

            )
        }
    }
}
