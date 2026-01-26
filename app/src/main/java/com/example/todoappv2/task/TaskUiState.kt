package com.example.todoappv2.task

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.components.TaskFilterType

data class TaskUiState(
    val isLoading: Boolean = false,
    val visibleTasks: List<TaskEntity> = emptyList(),
    val allTasks: List<TaskEntity> = emptyList(),
    val filter: TaskFilterType = TaskFilterType.ALL,
    val error: String? = null
)
