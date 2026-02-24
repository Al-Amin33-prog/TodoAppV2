package com.example.todoappv2.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository

import com.example.todoappv2.task.components.TaskFilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel (
    private val repository: AppRepository,
    private val scheduler: TaskReminderSchedule


): ViewModel(){
    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()
    private var currentFilter: TaskFilterType = TaskFilterType.All

    init {
       observeTasks()
    }

    private fun observeTasks(){
       viewModelScope.launch {
           repository.getAllTasks().collect { tasks ->
             updateStateWithTasks(tasks)

           }
       }
    }
    private fun updateStateWithTasks(tasks: List<TaskEntity>){
        _uiState.value =_uiState.value.copy(
            isLoading = false,
            allTasks = tasks,
            visibleTasks = applyFilterTasks(tasks,currentFilter)
        )

    }
    private fun applyFilterTasks(
        tasks: List<TaskEntity>,
        filter: TaskFilterType
    ): List<TaskEntity>{
        return when(filter){
            TaskFilterType.All -> tasks
            TaskFilterType.Completed -> tasks.filter { it.isCompleted }
            TaskFilterType.Pending -> tasks.filter { !it.isCompleted }
            is
                    TaskFilterType.BySubject -> tasks.filter {
                        it.subjectId == filter.subjectId
            }
        }
    }
    fun onEvent(event: TaskEvent){
        when(event){
            is TaskEvent.AddTask -> {
                viewModelScope.launch {
                   val task =      TaskEntity(
                            subjectId = event.subjectId,
                            title = event.title,
                            description = event.description,
                            dueDate = event.dueDate,
                            createdAt = System.currentTimeMillis()
                        )
                    repository.insertTask(task)
                    scheduler.scheduleTaskReminder(task)

                }
            }
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    repository.deleteTask(event.task)
                    scheduler.cancelTaskReminder(event.task.id)
                }
            }
            is TaskEvent.UpdateTask -> {
                viewModelScope.launch {
                    repository.updateTask(event.task)
                    scheduler.cancelTaskReminder(event.task.id)
                    scheduler.scheduleTaskReminder(event.task)
                }
            }
            is TaskEvent.ChangeFilter -> {
                currentFilter = event.filter
                val filtered = applyFilterTasks(
                    _uiState.value.allTasks,
                    event.filter
                )
                _uiState.value = _uiState.value.copy(
                    filter = event.filter,
                    visibleTasks = filtered
                )
            }
        }
    }
}
