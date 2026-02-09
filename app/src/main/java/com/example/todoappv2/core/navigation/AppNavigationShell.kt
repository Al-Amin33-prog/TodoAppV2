package com.example.todoappv2.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoappv2.core.components.AppBottomBar
import com.example.todoappv2.core.components.AppTopBar
import com.example.todoappv2.core.notification.components.NotificationScreen
import com.example.todoappv2.dashboard.HomeScreen
import com.example.todoappv2.settings.SettingsScreen
import com.example.todoappv2.statistics.StatisticScreen
import com.example.todoappv2.subject.SubjectScreen
import com.example.todoappv2.subject.SubjectViewModel
import com.example.todoappv2.task.TaskScreen
import com.example.todoappv2.task.TaskViewModel

@Composable
fun AppNavigationShell(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = { AppBottomBar(navController) },
        topBar = { AppTopBar() }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.HOME) {
                HomeScreen(navController)
            }

            composable(Routes.SUBJECTS) {
                val subjectViewModel: SubjectViewModel = viewModel()
                SubjectScreen(viewModel = subjectViewModel)
            }

            composable(Routes.TASKS) { backStackEntry ->
                val subjectId = backStackEntry.arguments
                    ?.getString("subjectId")
                    ?.toLongOrNull() ?: 0L

                val taskViewModel: TaskViewModel = viewModel()

                TaskScreen(
                    viewModel = taskViewModel,
                    subjectId = subjectId
                )
            }

            composable(Routes.STATS) {
                StatisticScreen()
            }

            composable(Routes.NOTIFICATIONS) {
                NotificationScreen()
            }

            composable(Routes.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}