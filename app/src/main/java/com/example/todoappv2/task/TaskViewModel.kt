package com.example.todoappv2.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository

import com.example.todoappv2.task.components.TaskFilterType
import com.example.todoappv2.task.components.TaskSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (
    private val repository: AppRepository,
    private val scheduler: TaskReminderSchedule


): ViewModel(){
    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private fun groupTasks(tasks: List<TaskEntity>): Map<TaskSection,List<TaskEntity>>{
        val now = System.currentTimeMillis()
        return tasks.groupBy { task ->
            when{
                task.dueDate == null ->
                    TaskSection.NoDate
                task.dueDate < now && !task.isCompleted ->
                    TaskSection.Overdue
                isToday(task.dueDate) ->
                    TaskSection.Today
                else ->
                    TaskSection.Upcoming
            }
        }
    }
    private fun isToday(time: Long): Boolean{
        val todayStart = java.time.LocalDate.now()
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val tomorrowStart = java.time.LocalDate.now()
            .plusDays(1)
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return time in todayStart until tomorrowStart
    }


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
        val filtered = applyFilterTasks(tasks, _uiState.value.filter)
        _uiState.value =_uiState.value.copy(
            isLoading = false,
            allTasks = tasks,
            visibleTasks = filtered,
            groupedTasks = groupTasks(filtered)

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
            TaskFilterType.Overdue ->
                tasks.filter {
                    it.dueDate != null &&
                            it.dueDate < System.currentTimeMillis() &&
                            !it.isCompleted
                }
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
                  val id =  repository.insertTask(task)
                    val tasksWithId = task.copy(id = id)
                    scheduler.scheduleTaskReminder(tasksWithId)



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

                val filtered = applyFilterTasks(
                    _uiState.value.allTasks,
                    event.filter
                )
                _uiState.value = _uiState.value.copy(
                    filter = event.filter,
                    visibleTasks = filtered,
                    groupedTasks = groupTasks(filtered)
                )
            }
            is TaskEvent.SearchTasks -> {
                _uiState.value = _uiState.value.copy(
                    searchQuery = event.query
                )
                val filtered = applyFilterTasks(
                    _uiState.value.allTasks,
                    _uiState.value.filter

                ).filter {
                    it.title.contains(event.query,
                        ignoreCase = true
                    )

                }
               _uiState.value =  _uiState.value.copy(
                   searchQuery = event.query,
                    visibleTasks = filtered,
                   groupedTasks = groupTasks(filtered)
                )
            }
            is TaskEvent.ToggleTaskCompletion -> {
                viewModelScope.launch {
                    val updatedTask = event.task.copy(
                        isCompleted = ! event.task.isCompleted
                    )
                    repository.updateTask(updatedTask)
                    if (updatedTask.isCompleted){
                        scheduler.cancelTaskReminder(updatedTask.id)
                    }
                }
            }
            is TaskEvent.RestoreTask -> {
                viewModelScope.launch {
                    repository.insertTask(event.task)
                }
            }
            is TaskEvent.ToggleSelectionMode  ->{
                _uiState.value = _uiState.value.copy(
                    isSelectionMode = true,
                    selectedTaskIds = emptySet()
                )

            }
            is TaskEvent.ToggleTaskSelection ->{
                val current = _uiState.value.selectedTaskIds.toMutableSet()
                if(current.contains(event.taskId)){
                    current.remove(event.taskId)
                }else{
                    current.add(event.taskId)
                }
               _uiState.value = _uiState.value.copy(
                    selectedTaskIds = current,
                   isSelectionMode = current.isNotEmpty()
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
        }
    }
}
