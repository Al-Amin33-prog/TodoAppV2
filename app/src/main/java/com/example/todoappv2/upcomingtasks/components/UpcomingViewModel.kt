package com.example.todoappv2.upcomingtasks.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UpcomingUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val isLoading: Boolean = true
)

class UpcomingViewModel(
  private val repository: AppRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UpcomingUiState())
    val uiState: StateFlow<UpcomingUiState> = _uiState
    init {
       loadUpcomingTasks()
    }
    fun loadUpcomingTasks(){
       viewModelScope.launch {
           repository.getUpcomingTasks(System.currentTimeMillis())
               .collect { tasks ->
                   _uiState.update { it.copy(tasks = tasks, isLoading = false) }
               }
       }
    }
}