package com.example.todoappv2.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoappv2.auth.AuthGateScreen
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.auth.LoginScreen
import com.example.todoappv2.auth.RegisterScreen
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    repository: AppRepository,
    schedule: TaskReminderSchedule

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
        composable(Routes.APP_SHEL) {
           AppNavigationShell(
               navController = navController,
               repository = repository,
               schedule = schedule
           )
        }
    }
}
