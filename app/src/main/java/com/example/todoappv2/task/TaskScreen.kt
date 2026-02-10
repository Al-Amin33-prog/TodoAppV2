package com.example.todoappv2.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.task.components.EmptyTaskState
import com.example.todoappv2.task.components.TaskFilter
import com.example.todoappv2.task.components.TaskList
import com.example.todoappv2.task.components.TaskProgressBar

@Composable
fun TaskScreen(
    subjectId: Long,
    repository: AppRepository,
    scheduler: TaskReminderSchedule
){
    val viewModel = remember(subjectId) {
        TaskViewModel(
            repository = repository,
            scheduler = scheduler,
            subjectId = subjectId
        )
    }
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