package com.example.todoappv2.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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
            _uiState.update { it.copy(isLoading = true) }
            _isRefreshing.value = true
            val startTime = System.currentTimeMillis()
            val subjects = repository.getSubjects().first()

            val allTasks = repository.getAllTasks().first()
            val subjectStats = subjects.map { subject ->
                val tasksForSubject = allTasks.filter { it.subjectId == subject.id }
                SubjectWithStats(
                    subject = subject,
                    totalTasks = tasksForSubject.size,
                    completedTasks = tasksForSubject.count { it.isCompleted },
                    pendingTasks = tasksForSubject.count { !it.isCompleted }


                )

            }
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.HOUR_OF_DAY,0)
            calendar.set(java.util.Calendar.MINUTE,0)
            calendar.set(java.util.Calendar.SECOND,0)
            calendar.set(java.util.Calendar.MILLISECOND,0)
            val startOfToday = calendar.timeInMillis
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
            val startOfTomorrow = calendar.timeInMillis



            val overDue = allTasks.filter {
                !it.isCompleted &&
                        it.dueDate != null &&
                        it.dueDate < startOfToday

            }
            val today = allTasks.filter {
                !it.isCompleted &&
                        it.dueDate != null &&
                        it.dueDate >= startOfToday &&
                        it.dueDate < startOfTomorrow

            }
            val upcoming = allTasks.filter {
                !it.isCompleted &&
                        it.dueDate != null &&
                        it.dueDate >= startOfTomorrow
            }.sortedBy{it.dueDate}
                .take(5)
            _uiState.value = HomeDashBoardUiState(
                isLoading = false,
                subjects = subjectStats,
                overDueTasks = overDue,
                todayTasks = today,
                upComingTasks = upcoming
            )
           val elapsed = System.currentTimeMillis() - startTime
            if (elapsed < 1200){
                delay(1200-elapsed)
            }
            _isRefreshing.value = false

            }



        }

    }






