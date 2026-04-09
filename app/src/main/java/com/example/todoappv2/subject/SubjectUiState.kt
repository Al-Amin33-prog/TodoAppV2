package com.example.todoappv2.subject

import com.example.todoappv2.data.local.entity.SubjectEntity

data class SubjectUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val subjects: List<SubjectEntity> = emptyList(),
    val error: String? = null,
    val isSelectionMode: Boolean = false,
    val selectedSubjectIds: Set<Long> = emptySet()
)
