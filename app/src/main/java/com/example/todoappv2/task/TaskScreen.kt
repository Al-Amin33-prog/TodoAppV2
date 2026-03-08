package com.example.todoappv2.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme


import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
            modifier = Modifier.padding(padding)
        ) {
            Text(
                "Tasks",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            TaskSearchBar(
                query = state.searchQuery,
                onQueryChanged = {
                    viewModel.onEvent(TaskEvent.SearchTasks(it))
                }
            )



            TaskFilter(
                selectedFilter = state.filter,
                onFilterSelected = {filter ->
                    viewModel.onEvent(TaskEvent.ChangeFilter(filter))
                }
            )
            when{
                state.isLoading ->{
                    TaskProgressBar()
                }
                state.visibleTasks.isEmpty() ->{
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
                            viewModel.onEvent(TaskEvent.DeleteTask(task))

                        },
                        onEditTask = {task ->
                            onEditTask(task.id)

                        }
                    )
                }
            }

        }



    }


}