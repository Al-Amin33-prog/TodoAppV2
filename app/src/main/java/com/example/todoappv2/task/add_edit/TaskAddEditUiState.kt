package com.example.todoappv2.task.add_edit

data class TaskAddEditUiState(
    val title: String ="",
    val description: String = "",
    val dueDate: Long? = null,
    val isCompleted: Boolean = false,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)