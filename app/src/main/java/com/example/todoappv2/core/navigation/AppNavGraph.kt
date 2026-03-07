package com.example.todoappv2.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoappv2.auth.AuthEvent
import com.example.todoappv2.auth.AuthGateScreen
import com.example.todoappv2.auth.AuthViewModel
import com.example.todoappv2.auth.components.login.LoginScreen
import com.example.todoappv2.auth.components.register.RegisterScreen
import com.example.todoappv2.auth.components.resetpassword.ResetPasswordScreen
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.settings.SettingsScreen
import com.example.todoappv2.subject.add_edit.SubjectAddEditScreen
import com.example.todoappv2.subject.add_edit.SubjectAddEditViewModel
import com.example.todoappv2.task.TaskScreen
import com.example.todoappv2.task.add_edit.TaskAddEditMode
import com.example.todoappv2.task.add_edit.TaskAddEditScreen
import com.example.todoappv2.task.add_edit.TaskAddEditViewModel
import com.example.todoappv2.task.add_edit.TaskAddEditViewModelFactory


@Composable
fun AppNavGraph(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    repository: AppRepository,
    schedule: TaskReminderSchedule,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    NavHost(
        navController = rootNavController,
        startDestination = Routes.AUTH_GATE
    ){
        composable(Routes.AUTH_GATE){
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
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    rootNavController.navigate(Routes.REGISTER)
                },

                onLoginSuccess = {
                 rootNavController.navigate(Routes.APP_SHELL){
                        popUpTo(Routes.LOGIN ){inclusive = true}
                    }
                },
                onForgotPassword = {
                    rootNavController.navigate("forgot_password")
                }
            )
        }
        composable("forgot_password"){
            ResetPasswordScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                   rootNavController.popBackStack()
                }
            )
        }

        composable(Routes.REGISTER){
           RegisterScreen(
               authViewModel = authViewModel,
               onBackToLogin = {
                   rootNavController.popBackStack()
               }
           )
        }
        // Headless route for logout action
        composable(Routes.LOGOUT){
            LaunchedEffect(Unit) {
                authViewModel.onEvent(AuthEvent.Logout)
                rootNavController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.AUTH_GATE) { inclusive = true }
                }
            }
        }
        composable(Routes.RESET_PASSWORD){
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
                    rootNavController.navigate(Routes.LOGOUT)
                },
                onBack = {
                 rootNavController.popBackStack()
                },

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
        ){backStackEntry ->
          val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: -1L
            val mode = if (taskId != -1L){
                TaskAddEditMode.Edit(taskId)
            }else{if (subjectId == -1L){
                throw IllegalArgumentException("Add task requires a subject")
            }
                TaskAddEditMode.Add(subjectId)
            }
           // val context = LocalContext.current
            val viewModel: TaskAddEditViewModel = viewModel(
                factory = TaskAddEditViewModelFactory(
                    repository = repository,
                    mode = mode ,
                    reminderSchedule = schedule
                )
            )
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
                navArgument("subjectId"){type = NavType.LongType}
            )
        ){backStackEntry ->
            val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: 0L
            TaskScreen(

                repository = repository,
                scheduler = schedule,
                onAddTask = {
                   rootNavController.navigate(Routes.addTask(subjectId))
                },
                onEditTask = {taskId ->
                 rootNavController.navigate(Routes.editTask(taskId,subjectId))
                }
            )

        }
        composable(
            Routes.EDIT_SUBJECT,
            arguments = listOf(
                navArgument("subjectId"){type = NavType.LongType}

            )
        ){backStackEntry ->
            val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: 0L
            val viewModel = remember(subjectId) {
                SubjectAddEditViewModel(repository = repository, subjectId = subjectId)
            }
            SubjectAddEditScreen(
                viewModel = viewModel,
                onDone = {
                    rootNavController.popBackStack()
                }
            )
        }
        composable(Routes.ADD_SUBJECT){
            val viewModel = remember {SubjectAddEditViewModel(repository)  }
            SubjectAddEditScreen(
                viewModel = viewModel,
                onDone = {
                    rootNavController.popBackStack()
                }
            )
        }


    }
}
