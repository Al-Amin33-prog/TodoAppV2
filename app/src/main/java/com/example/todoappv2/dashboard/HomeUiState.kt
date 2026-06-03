package com.example.todoappv2.dashboard

import androidx.compose.runtime.Immutable
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity

@Immutable
data class HomeDashBoardUiState(
    val isLoading: Boolean = true,
    val subjects: List<SubjectWithStats> = emptyList(),
    val upComingTasks: List<TaskEntity> = emptyList(),
    val todayTasks: List<TaskEntity> = emptyList(),
    val overDueTasks: List<TaskEntity> = emptyList(),

)
data class SubjectWithStats(
    val subject: SubjectEntity,
    val totalTasks: Int,
    val completedTasks : Int,
    val pendingTasks: Int
)