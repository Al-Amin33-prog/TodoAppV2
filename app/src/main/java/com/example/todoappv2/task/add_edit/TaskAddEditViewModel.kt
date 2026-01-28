package com.example.todoappv2.task.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskAddEditViewModel (
    private val repository: AppRepository,
    private val subjectId: Long,
    private val taskId: Long? = null
): ViewModel(){
    private val _uiState = MutableStateFlow(TaskAddEditUiState())
    val uiState: StateFlow<TaskAddEditUiState> = _uiState.asStateFlow()

    init {
        if (taskId != null){
            loadTask(taskId)
        }
    }

    private fun loadTask(taskId: Long){
       viewModelScope.launch {
           val task = repository.getTaskById(taskId) ?: return@launch
           _uiState.value = _uiState.value.copy(
               title = task.title,
               description = task.description ?: "",
               dueDate = task.dueDate,
               isCompleted = task.isCompleted,
               isEditing = true
           )
       }
    }

    fun onEvent(event: TaskAddEditEvent){
        when(event){
            is TaskAddEditEvent.TitleChanged -> {
                _uiState.value = _uiState.value.copy(title = event.value)
            }
            is TaskAddEditEvent.DescriptionChanged -> {
                _uiState.value = _uiState.value.copy(description = event.value)
            }
            is TaskAddEditEvent.DueDateChanged -> {
                _uiState.value = _uiState.value.copy(dueDate = event.value )
            }
            is TaskAddEditEvent.CompletionToggled -> {
                _uiState.value = _uiState.value.copy(isCompleted = event.value)
            }
            TaskAddEditEvent.SaveTask ->
                saveTask()
        }
    }

    private fun saveTask(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val state = _uiState.value
            if (state.isEditing){
                repository.updateTask(
                    TaskEntity(
                        id = taskId!!,
                        subjectId = subjectId,
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate,
                        isCompleted = state.isCompleted
                    )
                )
            } else {
                repository.insertTask(
                    TaskEntity(
                        subjectId = subjectId,
                        title = state.title,
                        description = state.description,
                        dueDate = state.dueDate
                    )
                )
            }
            _uiState.value = _uiState.value.copy(isSaving = false)
        }
    }
}