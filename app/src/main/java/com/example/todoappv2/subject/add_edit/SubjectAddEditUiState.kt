package com.example.todoappv2.subject.add_edit

data class SubjectAddEditUiState (
    val title: String = "",
    val colorHex: Long = 0xFF670A4,
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)