package com.example.todoappv2.task


import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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

    repository: AppRepository,
    scheduler: TaskReminderSchedule,
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit
){


    val viewModel = remember {
        TaskViewModel(
            repository = repository,
            scheduler = scheduler,

        )
    }
    val state = viewModel.uiState.collectAsState().value
    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }

    Scaffold(


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
            if (taskToDelete != null){
                AlertDialog(
                    onDismissRequest = {taskToDelete = null},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(
                                    TaskEvent.DeleteTask(taskToDelete!!)
                                )
                                taskToDelete = null
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {taskToDelete = null}
                        ) {
                            Text("Cancel")
                        }
                    },
                    title = {Text("Delete Task")},
                    text = {
                        Text("Are you sure you want to delete this task?")
                    }
                )
            }
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
                        onToggleCompleted = {task, completed->
                            viewModel.onEvent(
                                TaskEvent.UpdateTask(
                                    task.copy(isCompleted = completed)
                                )
                            )
                        },
                        onDelete = {task ->
                            taskToDelete = task

                        },
                        onEditTask = {task ->
                            onEditTask(task.id)

                        },
                        taskBeingDeleted = taskToDelete
                    )
                }
            }

        }



    }


}