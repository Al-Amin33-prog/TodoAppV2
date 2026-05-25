package com.example.todoappv2.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.domain.usecases.FilterTaskUseCases
import com.example.todoappv2.domain.usecases.GetTaskUseCases
import com.example.todoappv2.domain.usecases.GroupTaskUseCases
import com.example.todoappv2.domain.usecases.UiModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (
    private val repository: AppRepository,
    private val scheduler: TaskReminderSchedule,
    private val getTasks: GetTaskUseCases,
    private val filterTasks: FilterTaskUseCases,
    private val groupTasks: GroupTaskUseCases,
    private val mapToUi: UiModelUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks(){
        viewModelScope.launch {
            getTasks().collect { tasks ->
                _uiState.update { it.copy(allTasks = tasks) }
                refreshUiState()
            }
        }
    }

    private fun refreshUiState() {
        val currentState = _uiState.value
        
        // 1. Filter by status (Active/Completed/All)
        val filteredByStatus = filterTasks(currentState.allTasks, currentState.filter)
        
        // 2. Filter by search query
        val filteredBySearch = if (currentState.searchQuery.isBlank()) {
            filteredByStatus
        } else {
            filteredByStatus.filter { it.title.contains(currentState.searchQuery, ignoreCase = true) }
        }
        
        // 3. Map to UI Model and Group
        val uiTasks = filteredBySearch.map { mapToUi(it) }
        val grouped = groupTasks(uiTasks)
        
        _uiState.update { 
            it.copy(
                isLoading = false,
                visibleTasks = uiTasks,
                groupedTasks = grouped
            )
        }
    }

    fun onEvent(event: TaskEvent){
        when(event){
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    val task = _uiState.value.allTasks.find { it.id == event.taskId }
                    task?.let {
                        repository.deleteTask(it)
                        scheduler.cancelTaskReminder(it.id)
                    }
                }
            }
            is TaskEvent.ChangeFilter -> {
                _uiState.update { it.copy(filter = event.filter) }
                refreshUiState()
            }
            is TaskEvent.SearchTasks -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                refreshUiState()
            }
            is TaskEvent.ToggleTaskCompletion -> {
                viewModelScope.launch {
                    val task = _uiState.value.allTasks.find { it.id == event.taskId }
                    task?.let {
                        val updatedTask = it.copy(isCompleted = !it.isCompleted)
                        repository.updateTask(updatedTask)
                        if (updatedTask.isCompleted) {
                            scheduler.cancelTaskReminder(updatedTask.id)
                        }
                    }
                }
            }
            is TaskEvent.RestoreTask -> {
                viewModelScope.launch {
                    repository.insertTask(event.task)
                }
            }
            is TaskEvent.ToggleTaskSelection -> {
                val current = _uiState.value.selectedTaskIds
                val newSelection = if (event.taskId in current) current - event.taskId else current + event.taskId
                _uiState.update { it.copy(
                    selectedTaskIds = newSelection,
                    isSelectionMode = newSelection.isNotEmpty()
                )}
            }
            is TaskEvent.ClearSelection -> {
                _uiState.update { it.copy(isSelectionMode = false, selectedTaskIds = emptySet()) }
            }
            is TaskEvent.DeleteSelectedTasks -> {
                viewModelScope.launch {
                    val selectedIds = _uiState.value.selectedTaskIds
                    _uiState.value.allTasks.filter { it.id in selectedIds }.forEach { task ->
                        repository.deleteTask(task)
                        scheduler.cancelTaskReminder(task.id)
                    }
                    _uiState.update { it.copy(selectedTaskIds = emptySet(), isSelectionMode = false) }
                }
            }
            is TaskEvent.SelectAll -> {
                val allIds = _uiState.value.visibleTasks.map { it.id }.toSet()
                _uiState.update { it.copy(selectedTaskIds = allIds, isSelectionMode = allIds.isNotEmpty()) }
            }
            is TaskEvent.StartSelection -> {
                _uiState.update { it.copy(isSelectionMode = true, selectedTaskIds = setOf(event.taskId)) }
            }
            else -> Unit // AddTask/UpdateTask handled in AddEditViewModel
        }
    }
}
