package com.example.todoappv2.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel (
    private val repository: AppRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(HomeDashBoardUiState())
    val uiState: StateFlow<HomeDashBoardUiState> = _uiState.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    init {
        refresh()
    }
    fun refresh(){
        viewModelScope.launch {
            _isRefreshing.value = true
            _uiState.value = _uiState.value.copy(isLoading = true)
            val subjects = repository.getSubjects().first()

            val allTasks = mutableListOf<TaskEntity>()
            val subjectStats = mutableListOf<SubjectWithStats>()

            for (subject in subjects){
                val tasks = repository
                    .getTasKBySubject(subject.id)
                    .first()

                allTasks.addAll(tasks)
                subjectStats.add(
                    SubjectWithStats(
                        subject = subject,
                        totalTasks = tasks.size,
                        completedTasks = tasks.count{it.isCompleted},
                        pendingTasks = tasks.count { !it.isCompleted }
                    )
                )

            }
            val now = System.currentTimeMillis()
            val overDue = allTasks.filter {
                !it.isCompleted &&
                        it.dueDate != null &&
                        it.dueDate < now

            }
            val today = allTasks.filter {
                !it.isCompleted &&
                        it.dueDate != null &&
                        isSameDay(it.dueDate,now)
            }
            val upcoming = allTasks.filter {
                !it.isCompleted &&
                        it.dueDate != null &&
                        it.dueDate > now
            }.sortedBy{it.dueDate}
            _uiState.value = HomeDashBoardUiState(
                isLoading = false,
                subjects = subjectStats,
                overDueTasks = overDue,
                todayTasks = today,
                upComingTasks = upcoming
            )
            _isRefreshing.value = false

        }

    }



}

    private fun isSameDay(time1: Long, time2: Long): Boolean{
        val cal1 = java.util.Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = java.util.Calendar.getInstance().apply { timeInMillis = time2 }

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }
