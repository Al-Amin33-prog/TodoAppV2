package com.example.todoappv2.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.todoappv2.auth.AuthViewModel
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
    val authViewModel: AuthViewModel = hiltViewModel()
    val appNavController = rememberNavController()
    val navBackStackEntry by appNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val authState by authViewModel.uiState.collectAsState()

    
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
                title = {
                    title
                },
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

                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    userName = authState.user?.name?: "User",
                    viewModel = homeViewModel,
                    onAddTaskClick = {
                        navController.navigate(Routes.addTask(0L))
                    },
                    onAddSubjectClick = {
                        navController.navigate(Routes.ADD_SUBJECT)
                    },
                    onTaskClick = { taskId ->
                        navController.navigate(Routes.editTask(taskId, 0L))
                    },
                )
            }

            composable(Routes.SUBJECTS) {

                val subjectViewModel: SubjectViewModel = hiltViewModel()
                SubjectScreen(
                    viewModel = subjectViewModel,
                    onAddSubject = {
                       navController.navigate(Routes.ADD_SUBJECT)
                    },
                    onOpenSubject = { subjectId ->
                        appNavController.navigate(Routes.tasksWithId(subjectId))
                    },
                    onEditSubject = { subjectId ->
                       navController.navigate(Routes.editSubject(subjectId))
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
                        navController.navigate(Routes.addTask(subjectId))
                    },
                    onEditTask = { taskId ->
                       navController.navigate(Routes.editTask(taskId, subjectId))
                    }
                )
            }


            composable(Routes.TASKS_ROOT){
                TaskScreen(

                    onAddTask = {
                        navController.navigate(Routes.addTask(0L))
                    },
                    onEditTask = { taskId ->
                       navController.navigate(
                           Routes.editTask(taskId,0L)
                       )
                    }
                )
            }
            composable(Routes.STATS){
                StatisticScreen()
            }


        }
    }
}
