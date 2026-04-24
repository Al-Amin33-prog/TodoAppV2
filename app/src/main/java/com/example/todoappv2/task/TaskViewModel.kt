package com.example.todoappv2.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.domain.usecases.FilterTaskUseCases
import com.example.todoappv2.domain.usecases.GetTaskUseCases
import com.example.todoappv2.domain.usecases.GroupTaskUseCases
import com.example.todoappv2.domain.usecases.UiModelUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
          val filtered = filterTasks(tasks,_uiState.value.filter)
                val uiTasks = filtered.map{mapToUi(it)}
                val grouped = groupTasks(uiTasks)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allTasks = tasks,
                    visibleTasks =uiTasks,
                    groupedTasks = grouped
                )

            }
        }
    }


    fun startSelection(taskId: Long){
        _uiState.value = _uiState.value.copy(
            isSelectionMode = true,
            selectedTaskIds = setOf(taskId)
        )
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
                    val id =  repository.insertTask(task)
                    val tasksWithId = task.copy(id = id)
                    scheduler.scheduleTaskReminder(tasksWithId)



                }
            }
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    val task = _uiState.value.allTasks.find { it.id == event.taskId }
                   task?.let {
                       repository.deleteTask(it)
                       scheduler.cancelTaskReminder(it.id)
                   }
                }
            }
            is TaskEvent.UpdateTask -> {
                viewModelScope.launch {
                    val task = _uiState.value.allTasks.find { it.id == event.taskId }
                    task?.let {
                        repository.updateTask(it)
                        scheduler.cancelTaskReminder(it.id)
                        scheduler.scheduleTaskReminder(it)
                    }
                }
            }
            is TaskEvent.ChangeFilter -> {

                val filtered = filterTasks(
                    _uiState.value.allTasks,
                    event.filter
                )
                val uiTasks = filtered.map{mapToUi(it)}
                _uiState.value = _uiState.value.copy(
                    filter = event.filter,
                    visibleTasks = uiTasks,
                    groupedTasks = groupTasks(uiTasks)
                )
            }
            is TaskEvent.SearchTasks -> {
                _uiState.value = _uiState.value.copy(
                    searchQuery = event.query
                )
                val filtered = filterTasks(
                    _uiState.value.allTasks,
                    _uiState.value.filter

                ).filter {
                    it.title.contains(event.query,
                        ignoreCase = true
                    )

                }
                val uiTasks = filtered.map { mapToUi(it) }
                _uiState.value =  _uiState.value.copy(
                    searchQuery = event.query,
                    visibleTasks = uiTasks,
                    groupedTasks = groupTasks(uiTasks)
                )
            }
            is TaskEvent.ToggleTaskCompletion -> {
                viewModelScope.launch {
                    val task = _uiState.value.allTasks.find { it.id == event.taskId }
                    task?.let {
                        val updatedTask = it.copy(isCompleted = !it.isCompleted)
                        repository.updateTask(updatedTask)
                        if (updatedTask.isCompleted){
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

            is TaskEvent.ToggleTaskSelection ->{
                val current = _uiState.value.selectedTaskIds
                val newSelection = if (event.taskId in current){
                    current - event.taskId
                }else{
                    current + event.taskId
                }
                _uiState.value = _uiState.value.copy(
                    selectedTaskIds = newSelection,
                    isSelectionMode = newSelection.isNotEmpty()
                )

            }
            is TaskEvent.ClearSelection ->{
                _uiState.value = _uiState.value.copy(
                    isSelectionMode = false,
                    selectedTaskIds = emptySet()
                )
            }
            is TaskEvent.DeleteSelectedTasks -> {
                viewModelScope.launch {
                    val selectedIds = _uiState.value.selectedTaskIds
                    _uiState.value.allTasks.filter {
                        it.id in  selectedIds
                    }
                        .forEach { task ->
                            repository.deleteTask(task)
                            scheduler.cancelTaskReminder(task.id)
                        }
                    _uiState.value = _uiState.value.copy(
                        selectedTaskIds = emptySet(),
                        isSelectionMode = false

                    )
                }
            }
            is TaskEvent.SelectAll -> {
                val allIds = _uiState.value.visibleTasks.map { it.id }.toSet()
                _uiState.value = _uiState.value.copy(
                    selectedTaskIds = allIds,
                    isSelectionMode = allIds.isNotEmpty()
                )
            }
            is TaskEvent.StartSelection -> {
                _uiState.value = _uiState.value.copy(
                    isSelectionMode = true,
                    selectedTaskIds = setOf(event.taskId)
                )
            }
        }
    }
}
