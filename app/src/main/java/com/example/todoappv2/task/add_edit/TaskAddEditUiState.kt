package com.example.todoappv2.task.add_edit

import androidx.compose.runtime.Immutable


@Immutable
data class TaskAddEditUiState(

    val title: String ="",
    val description: String = "",
    val dueDate: Long? = null,
    val subjectId: Long? = null,
    val subjectName: String ="",
    val priority: String = "Medium",
    val isCompleted: Boolean = false,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val predictedPriority: String = "Medium",
    val predictionConfidence: Float = 0f,
    val isPredictionLoading: Boolean = false,
    val isPriorityOverridden: Boolean = false,


)