package com.example.todoappv2.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoappv2.core.components.AppBottomBar
import com.example.todoappv2.core.components.AppTopBar
import com.example.todoappv2.dashboard.HomeScreen
import com.example.todoappv2.dashboard.HomeViewModel
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

) {
    val appNavController = rememberNavController()
    val navBackStackEntry by appNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val title = when {
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
        bottomBar = { AppBottomBar(appNavController) },
        topBar = { 
            AppTopBar(
                title = title,
                showBackButton = false,
                onBackClick = {},
                onSettingClick = {
                    navController.navigate(Routes.SETTINGS)
                }
            ) 
        }
    ) { padding ->

        NavHost(
            navController = appNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.HOME) {
                // Now using hiltViewModel() instead of manual creation
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = homeViewModel,
                    onAddTaskClick = {
                        appNavController.navigate(Routes.addTask(0L))
                    },
                    onAddSubjectClick = {
                        appNavController.navigate(Routes.ADD_SUBJECT)
                    },
                    onTaskClick = { taskId ->
                        appNavController.navigate(Routes.addTask(taskId))
                    },
                )
            }

            composable(Routes.SUBJECTS) {
                // Update this to use hiltViewModel() once SubjectViewModel is refactored
                val subjectViewModel: SubjectViewModel = hiltViewModel()
                SubjectScreen(
                    viewModel = subjectViewModel,
                    onAddSubject = {
                        appNavController.navigate(Routes.ADD_SUBJECT)
                    },
                    onOpenSubject = { subjectId ->
                        appNavController.navigate(Routes.tasksWithId(subjectId))
                    },
                    onEditSubject = { subjectId ->
                        appNavController.navigate(Routes.editSubject(subjectId))
                    }
                )
            }
            
            composable(Routes.ADD_SUBJECT){
                val viewModel: SubjectAddEditViewModel = hiltViewModel()
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
                    navArgument("subjectId"){ type = NavType.LongType }
                )
            ){ backStackEntry ->
                val viewModel: SubjectAddEditViewModel = hiltViewModel()
                SubjectAddEditScreen(
                    viewModel = viewModel,
                    onDone = {
                        appNavController.popBackStack()
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
                        appNavController.navigate(Routes.addTask(subjectId))
                    },
                    onEditTask = { taskId ->
                       appNavController.navigate(Routes.editTask(taskId, subjectId))
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
                        appNavController.popBackStack()
                    },
                    onCancel = {
                        appNavController.popBackStack()
                    }
                )
            }

            composable(Routes.STATS) {
                StatisticScreen(

                )
            }

            composable(Routes.TASKS_ROOT){
                TaskScreen(

                    onAddTask = {
                        appNavController.navigate(Routes.addTask(0L))
                    },
                    onEditTask = { taskId ->
                        appNavController.navigate("edit_task/$taskId")
                    }
                )
            }
        }
    }
}
