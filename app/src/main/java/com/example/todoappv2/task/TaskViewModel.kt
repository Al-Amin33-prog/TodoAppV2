package com.example.todoappv2.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository

import com.example.todoappv2.task.components.TaskFilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel (
    private val repository: AppRepository,
    private val subjectId: Long,

): ViewModel(){
    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
       observeTasks()
    }

    private fun observeTasks(){
       viewModelScope.launch {
           repository.getTasKBySubject(subjectId).collect { tasks ->
              val filter = _uiState.value.filter
               _uiState.value =_uiState.value.copy(
                   isLoading = false,
                   allTasks = tasks,
                   visibleTasks = applyFilterTasks(tasks,filter)
               )


           }
       }
    }
    private fun applyFilterTasks(
        tasks: List<TaskEntity>,
        filter: TaskFilterType
    ): List<TaskEntity>{
        return when(filter){
            TaskFilterType.ALL -> tasks
            TaskFilterType.COMPLETED -> tasks.filter { it.isCompleted }
            TaskFilterType.PENDING -> tasks.filter { !it.isCompleted }
        }
    }
    fun onEvent(event: TaskEvent){
        when(event){
            is TaskEvent.AddTask -> {
                viewModelScope.launch {
                    repository.insertTask(
                        TaskEntity(
                            subjectId = event.subjectId,
                            title = event.title,
                            description = event.description,
                            dueDate = event.dueDate,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                }
            }
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    repository.deleteTask(event.task)
                }
            }
            is TaskEvent.UpdateTask -> {
                viewModelScope.launch {
                    repository.updateTask(event.task)
                }
            }
            is TaskEvent.ChangeFilter -> {
                val tasks = uiState.value.allTasks
                _uiState.value = _uiState.value.copy(
                    filter = event.filter,
                    visibleTasks = applyFilterTasks(tasks, event.filter)
                )
            }
        }
    }
}
