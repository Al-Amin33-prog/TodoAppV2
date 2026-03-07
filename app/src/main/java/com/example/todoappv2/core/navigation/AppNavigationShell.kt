package com.example.todoappv2.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todoappv2.core.components.AppBottomBar
import com.example.todoappv2.core.components.AppTopBar
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.dashboard.HomeScreen
import com.example.todoappv2.dashboard.HomeViewModel
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.statistics.StatisticScreen
import com.example.todoappv2.subject.SubjectScreen
import com.example.todoappv2.subject.SubjectViewModel
import com.example.todoappv2.task.TaskScreen



@Composable
fun AppNavigationShell(
    navController: NavHostController,
    repository: AppRepository,
    schedule: TaskReminderSchedule,
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    val appNavController = rememberNavController()
    val navBackStackEntry by appNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val hideBars =  currentRoute?.startsWith("add_edit_task") == true ||
    currentRoute?.startsWith("edit_subject") == true ||
    currentRoute == Routes.ADD_SUBJECT
    val title = when{
        currentRoute?.startsWith(Routes.TASKS_ROOT) == true -> "Tasks"
currentRoute == Routes.SUBJECTS -> "Subjects"
        currentRoute == Routes.STATS -> "Statistics"
        currentRoute == Routes.HOME -> "Home"
        currentRoute == Routes.ADD_SUBJECT -> "Add Subject"
        currentRoute?.startsWith(Routes.EDIT_SUBJECT) == true -> "Edit Subject"
        currentRoute?.startsWith(Routes.ADD_EDIT_TASK) == true -> "Add Edit Task"

        else -> ""
    }
    Scaffold(
        bottomBar = { if (!hideBars){
            AppBottomBar(appNavController)
        } },
        topBar = { AppTopBar(
            title = title,
            showBackButton = false,
            onBackClick = {navController.popBackStack()},
            onSettingClick = {
                navController.navigate(Routes.SETTINGS)
            }
        ) }
    ) { padding ->

        NavHost(
            navController = appNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.HOME) {
                val homeViewModel = remember {HomeViewModel(repository)  }
                HomeScreen(
                    viewModel = homeViewModel,
                    onAddTaskClick = {
                       navController.navigate(Routes.addTask(0L))
                    },
                    onAddSubjectClick = {
                   navController.navigate(Routes.ADD_SUBJECT)
                    },
                    onTaskClick = {taskId ->
                        navController.navigate(Routes.editTask(taskId, 0L))
                    },


                )
            }

            composable(Routes.SUBJECTS) {
                val subjectViewModel = remember{ SubjectViewModel(repository)}
                SubjectScreen(
                    viewModel = subjectViewModel,
                    onAddSubject = {
                       navController.navigate(Routes.ADD_SUBJECT)

                    },
                    onOpenSubject = { subjectId ->
                        appNavController.navigate(
                            Routes.tasksWithId(subjectId)
                        )

                    },
                    onEditSubject = {subjectId ->
                        navController.navigate(Routes.editSubject(subjectId))
                    }

                )
            }


            composable(Routes.STATS) {
                StatisticScreen()
            }

            composable(Routes.TASKS_ROOT){
                TaskScreen(

                    repository = repository,
                    scheduler = schedule,
                    onAddTask = {
                        navController.navigate(Routes.addTask(0L))
                    },
                    onEditTask = {taskId ->
                        navController.navigate(Routes.editTask(taskId,0L))
                    }
                )
            }
        }
    }
}
