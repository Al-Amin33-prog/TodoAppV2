package com.example.todoappv2.task


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.task.components.EmptyTaskState
import com.example.todoappv2.task.components.TaskFilter
import com.example.todoappv2.task.components.TaskList
import com.example.todoappv2.task.components.TaskProgressBar
import com.example.todoappv2.R
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.components.TaskSearchBar

@Composable
fun TaskScreen(
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit
){


   val viewModel: TaskViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    var recentlyDeletedTask by remember { mutableStateOf<TaskEntity?>(null) }
    LaunchedEffect(recentlyDeletedTask) {
        recentlyDeletedTask?.let { task ->
            val result = snackbarHostState.showSnackbar(
                message = "Task deleted",
                actionLabel = "Undo"
            )
            if (result == SnackbarResult.ActionPerformed){
                viewModel.onEvent(TaskEvent.RestoreTask(task))
            }
            recentlyDeletedTask = null
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },


        floatingActionButton = {
            FloatingActionButton(onClick = {onAddTask()}) {
                Icon(painter = painterResource(R.drawable.add_task_24px),
                    contentDescription = "add task"
                )
            }
        }
    ) { padding->
        Column(
            modifier = Modifier
                .padding(padding),

        ) {

            TaskSearchBar(
                query = state.searchQuery,
                onQueryChanged = {
                    viewModel.onEvent(TaskEvent.SearchTasks(it))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TaskFilter(
                selectedFilter = state.filter,
                onFilterSelected = {filter ->
                    viewModel.onEvent(TaskEvent.ChangeFilter(filter))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            when{
                state.isLoading ->{
                    TaskProgressBar()
                }
                state.groupedTasks.isEmpty() ->{
                    EmptyTaskState()
                }
                else -> {
                    TaskList(
                     groupedTasks = state.groupedTasks,
                        onToggleCompleted = {task ->
                            viewModel.onEvent(
                                TaskEvent.ToggleTaskCompletion(task)
                            )
                        },
                        onDelete = {task ->
                          viewModel.onEvent(TaskEvent.DeleteTask(task))
                            recentlyDeletedTask = task

                        },
                        onEditTask = {task ->
                            onEditTask(task.id)

                        },
                        taskBeingDeleted = recentlyDeletedTask
                    )
                }
            }

        }



    }


}