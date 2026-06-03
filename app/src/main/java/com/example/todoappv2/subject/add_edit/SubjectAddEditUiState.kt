package com.example.todoappv2.subject.add_edit

import androidx.compose.runtime.Immutable

@Immutable
data class SubjectAddEditUiState (
    val title: String = "",
    val colorHex: Long = 0xFF4CAF50,
    val createdAt: Long = 0L,
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)