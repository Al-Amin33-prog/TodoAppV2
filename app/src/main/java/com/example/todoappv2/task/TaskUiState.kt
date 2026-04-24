package com.example.todoappv2.task

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.components.TaskFilterType
import com.example.todoappv2.task.components.TaskSection

data class TaskUiState(
    val isLoading: Boolean = false,
    val visibleTasks: List<TaskUiModel> = emptyList(),
    val allTasks: List<TaskEntity> = emptyList(),
    val filter: TaskFilterType = TaskFilterType.All,
    val searchQuery: String = "",
    val groupedTasks: Map<TaskSection, List<TaskUiModel>> = emptyMap(),
    val error: String? = null,
    val isSelectionMode: Boolean = false,
    val selectedTaskIds: Set<Long> = emptySet()
)
