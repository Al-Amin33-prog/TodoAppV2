package com.example.todoappv2.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.todoappv2.task.components.EmptyTaskState
import com.example.todoappv2.task.components.TaskFilter
import com.example.todoappv2.task.components.TaskList
import com.example.todoappv2.task.components.TaskProgressBar

@Composable
fun TaskScreen(
    viewModel: TaskViewModel
){
    val state = viewModel.uiState.collectAsState().value
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
                tasks = state.visibleTasks,
                onDelete = {task ->
                    viewModel.onEvent(TaskEvent.DeleteTask(task))

                }
            )
        }
    }


}