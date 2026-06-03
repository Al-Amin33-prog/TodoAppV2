package com.example.todoappv2.subject

import androidx.compose.runtime.Immutable
import com.example.todoappv2.data.local.entity.SubjectEntity
@Immutable
data class SubjectUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val subjects: List<SubjectEntity> = emptyList(),
    val error: String? = null,
    val isSelectionMode: Boolean = false,
    val selectedSubjectIds: Set<Long> = emptySet()
)
