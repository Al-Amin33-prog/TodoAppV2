package com.example.todoappv2.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoappv2.core.components.AppBottomBar
import com.example.todoappv2.core.components.AppTopBar
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.core.notification.components.NotificationScreen
import com.example.todoappv2.dashboard.HomeScreen
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.settings.SettingsScreen
import com.example.todoappv2.statistics.StatisticScreen
import com.example.todoappv2.subject.SubjectScreen
import com.example.todoappv2.subject.SubjectViewModel
import com.example.todoappv2.subject.add_edit.SubjectAddEditScreen
import com.example.todoappv2.subject.add_edit.SubjectAddEditViewModel
import com.example.todoappv2.task.TaskScreen
import com.example.todoappv2.task.add_edit.TaskAddEditScreen
import com.example.todoappv2.task.add_edit.TaskAddEditViewModel


@Composable
fun AppNavigationShell(
    navController: NavHostController,
    repository: AppRepository,
    schedule: TaskReminderSchedule,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val appNavController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomBar(appNavController) },
        topBar = { AppTopBar(appNavController) }
    ) { padding ->

        NavHost(
            navController = appNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.HOME) {
                HomeScreen(appNavController)
            }

            composable(Routes.SUBJECTS) {
                val subjectViewModel: SubjectViewModel = viewModel()
                SubjectScreen(
                    viewModel = subjectViewModel,
                    onAddSubject = {
                        appNavController.navigate(Routes.ADD_SUBJECT)

                    },
                    onOpenSubject = { subjectId ->
                        appNavController.navigate(
                            Routes.tasksWithId(subjectId)
                        )

                    },
                    onEditSubject = {subjectId ->
                        appNavController.navigate(Routes.editSubject(subjectId))
                    }

                )
            }
            composable(Routes.ADD_SUBJECT){
                val viewModel: SubjectAddEditViewModel = viewModel()
                SubjectAddEditScreen(
                    viewModel = viewModel,
                    onDone = {
                        appNavController.popBackStack()
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
                val viewModel: SubjectAddEditViewModel = viewModel()
                SubjectAddEditScreen(
                    viewModel = viewModel,
                    onDone = {
                        appNavController.popBackStack()
                    }
                )
            }

            composable(Routes.TASKS) { backStackEntry ->
                val subjectId = backStackEntry.arguments
                    ?.getString("subjectId")
                    ?.toLongOrNull() ?: 0L


                TaskScreen(
                    subjectId = subjectId,
                    repository = repository,
                    scheduler = schedule,
                    onAddTask = {
                        appNavController.navigate(
                            Routes.addTask(subjectId)
                        )

                    },
                    onEditTask = {taskId ->
                        appNavController.navigate(
                            Routes.editTask(taskId)
                        )

                    }
                )
            }
            composable(
                Routes.ADD_TASK,
                arguments = listOf(
                    navArgument("subjectId"){type = NavType.LongType}
                )
            ){backStackEntry ->
                val subjectId = backStackEntry.arguments?.getLong("subjectId") ?: 0L
                val viewModel = TaskAddEditViewModel(
                    repository = repository,
                   subjectId = subjectId
                )
                TaskAddEditScreen(
                    viewModel = viewModel,
                    onDone = {
                        appNavController.popBackStack()
                    }

                )
            }
            composable(
                Routes.EDIT_TASK,
                arguments = listOf(
                    navArgument("taskId"){type = NavType.LongType}
                )
            ){backStackEntry ->
                val taskId = backStackEntry.arguments?.getLong("taskId") ?: 0L
                val viewModel = TaskAddEditViewModel(
                    repository = repository,
                   taskId = taskId
                )
                TaskAddEditScreen(
                    viewModel = viewModel,
                    onDone = {
                        appNavController.popBackStack()
                    }
                )

            }

            composable(Routes.STATS) {
                StatisticScreen()
            }

            composable(Routes.NOTIFICATIONS) {
                NotificationScreen()
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                   appNavController = appNavController,
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange
                )
            }
        }
    }
}
