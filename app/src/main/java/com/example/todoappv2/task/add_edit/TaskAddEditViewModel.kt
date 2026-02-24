package com.example.todoappv2.task.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskAddEditViewModel (
    private val repository: AppRepository,

    private val taskId: Long? = null,
    private val subjectId: Long? = null
): ViewModel(){

    private val _uiState = MutableStateFlow(TaskAddEditUiState(subjectId = subjectId))
    val uiState: StateFlow<TaskAddEditUiState> = _uiState.asStateFlow()
    val subjects = repository.getSubjects()
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    sealed class UiEvent{
        object SaveSuccess: UiEvent()
        data class ShowError(val message: String): UiEvent()
    }

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
               subjectId = task.subjectId,
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
           is TaskAddEditEvent.SubjectChanged ->{
               _uiState.value =_uiState.value.copy(
                   subjectId = event.subjectId, subjectName = event.subjectName
               )
           }
            is TaskAddEditEvent.PriorityChanged ->{
                _uiState.value = _uiState.value.copy(
                    priority = event.value
                )
            }
            TaskAddEditEvent.SaveTask ->
                saveTask()
        }
    }

    private fun saveTask(){
        viewModelScope.launch {
            val state = _uiState.value

       val realSubjectId = state.subjectId ?: 0L
            if (realSubjectId == 0L){
                _uiEvent.emit(UiEvent
                    .ShowError("Please select a subject"))
                return@launch
            }
            if (state.title.isBlank()){
                _uiEvent.emit(UiEvent
                    .ShowError("Title cannot be blank"))
                return@launch
            }
            _uiState.value = state.copy(isSaving = true)
            val entity = TaskEntity(
                id = if (state.isEditing)taskId ?: 0 else 0,
                subjectId = realSubjectId,
                title = state.title,
                description = state.description,
                dueDate = state.dueDate,
                isCompleted = state.isCompleted
            )
            if (state.isEditing)
                repository.updateTask(entity)
            else
                repository.insertTask(entity)
            _uiState.value = state.copy(isSaving =  false)
            _uiEvent.emit(UiEvent.SaveSuccess)
    }
}
}