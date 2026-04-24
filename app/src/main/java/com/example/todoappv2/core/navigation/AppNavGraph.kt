package com.example.todoappv2.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthGateScreen
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.auth.components.EmailVerificationPendingScreen
import com.example.todoappv2.auth.components.login.LoginScreen
import com.example.todoappv2.auth.components.register.RegisterScreen
import com.example.todoappv2.auth.components.resetpassword.ResetPasswordScreen
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.settings.SettingsScreen
import com.example.todoappv2.subject.add_edit.SubjectAddEditScreen
import com.example.todoappv2.subject.add_edit.SubjectAddEditViewModel
import com.example.todoappv2.task.TaskScreen
import com.example.todoappv2.task.add_edit.TaskAddEditScreen
import com.example.todoappv2.task.add_edit.TaskAddEditViewModel

@Composable
fun AppNavGraph(
    rootNavController: NavHostController,
    schedule: TaskReminderSchedule,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    NavHost(
        navController = rootNavController,
        startDestination = Routes.AUTH_GATE
    ){
        composable(Routes.AUTH_GATE){
            val authViewModel: AuthViewModel = hiltViewModel()
            AuthGateScreen(
                authViewModel = authViewModel,
                onAuthenticated = {
                  rootNavController.navigate(Routes.APP_SHELL){
                        popUpTo(Routes.AUTH_GATE){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onUnAuthenticated = {
                  rootNavController.navigate(Routes.LOGIN){
                        popUpTo(Routes.AUTH_GATE){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    rootNavController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    rootNavController.navigate(
                        Routes.APP_SHELL
                    ){
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },



                onForgotPassword = {
                    rootNavController.navigate(Routes.RESET_PASSWORD)
                }
            )
        }
        composable(Routes.REGISTER){
           val authViewModel: AuthViewModel = hiltViewModel()
           RegisterScreen(
               authViewModel = authViewModel,
               onBackToLogin = {
                   rootNavController.popBackStack()
               },
               onRegisterSuccess = {
                   rootNavController.navigate("email_verification")
               }
           )
        }
        composable(Routes.LOGOUT){
            val authViewModel: AuthViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                authViewModel.onEvent(AuthEvent.Logout)
                rootNavController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
        composable(Routes.RESET_PASSWORD){
            val authViewModel: AuthViewModel = hiltViewModel()
            ResetPasswordScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable(Routes.APP_SHELL) {
           AppNavigationShell(
               navController = rootNavController,


           )
        }
        composable(Routes.SETTINGS){
            SettingsScreen(

                isDarkMode = isDarkMode,
                onThemeChange = onThemeChange,
                onLogout = {
                    rootNavController.navigate(Routes.LOGOUT)
                },
                onBack = {
                    rootNavController.popBackStack()
                }
            )
        }
        
        composable(
            Routes.ADD_EDIT_TASK,
            arguments = listOf(
                navArgument("taskId"){
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("subjectId"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val viewModel: TaskAddEditViewModel = hiltViewModel()
            TaskAddEditScreen(
                viewModel = viewModel,
                onDone = {
                    rootNavController.popBackStack()
                },
                onCancel = {
                    rootNavController.popBackStack()
                }
            )
        }
        
        composable(
            Routes.TASKS_BY_SUBJECT,
            arguments = listOf(
                navArgument("subjectId"){ type = NavType.LongType }
            )
        ){ backStackEntry ->
            val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: 0L
            TaskScreen(

                onAddTask = {
                   rootNavController.navigate(Routes.addTask(subjectId))
                },
                onEditTask = { taskId ->
                 rootNavController.navigate(Routes.editTask(taskId, subjectId))
                },

            )
        }
        
        composable(
            Routes.EDIT_SUBJECT,
            arguments = listOf(
                navArgument("subjectId"){ type = NavType.LongType }
            )
        ){
            val viewModel: SubjectAddEditViewModel = hiltViewModel()
            SubjectAddEditScreen(
                viewModel = viewModel,
                onDone = {
                    rootNavController.popBackStack()
                }
            )
        }
        
        composable(Routes.ADD_SUBJECT){
            val viewModel: SubjectAddEditViewModel = hiltViewModel()
            SubjectAddEditScreen(
                viewModel = viewModel,
                onDone = {
                    rootNavController.popBackStack()
                }
            )
        }
        composable(Routes.EMAIL_VERIFICATION){
            val authViewModel: AuthViewModel = hiltViewModel()
            EmailVerificationPendingScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                    rootNavController.popBackStack()
                }

            )
        }
    }
}
