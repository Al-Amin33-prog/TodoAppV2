package com.example.todoappv2.task.add_edit

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.ml.MLHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskAddEditViewModel @Inject constructor(
    private val repository: AppRepository,
    private val reminderSchedule: TaskReminderSchedule,
    private val mlHelper: MLHelper,

    savedStateHandle: SavedStateHandle
): ViewModel(){

    private val taskId: Long? = savedStateHandle.get<Long>("taskId")?.takeIf { it != -1L }
    private val subjectId: Long? = savedStateHandle.get<Long>("subjectId")?.takeIf { it != -1L }

    private val _uiState = MutableStateFlow(TaskAddEditUiState())
    val uiState: StateFlow<TaskAddEditUiState> = _uiState.asStateFlow()
    //  CORRECT - Job is created internally
    private var predictionJob: Job? = null
    
    val subjects = repository.getSubjects()
    
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    


    sealed class UiEvent{
        object SaveSuccess: UiEvent()
        data class ShowError(val message: String): UiEvent()
    }

    init {
        if (taskId != null) {
            loadTask(taskId)
        } else if (subjectId != null) {
            loadSubject(subjectId)
        }
        viewModelScope.launch {
            mlHelper.initializeModel()
        }
    }
    private fun validate(state: TaskAddEditUiState): String?{
        return when{
            state.subjectId == null ->
                "Please select a subject"
            state.title.isBlank() ->
                "Title cannot be blank"
            else -> null
        }
    }

    private fun loadSubject(id: Long) {
        viewModelScope.launch {
            val subject = repository.getSubjectById(id)
            subject?.let { subjectEntity ->
                _uiState.update { state ->
                    state.copy(
                        subjectId = subjectEntity.id,
                        subjectName = subjectEntity.name
                    )
                }
            }
        }
    }

    private fun loadTask(taskId: Long){
       viewModelScope.launch {
           val task = repository.getTaskById(taskId) ?: return@launch
           val subject = repository.getSubjectById(task.subjectId)
           _uiState.update { 
               it.copy(
                   title = task.title,
                   description = task.description ?: "",
                   dueDate = task.dueDate,
                   isCompleted = task.isCompleted,
                   subjectName = subject?.name ?: "",
                   subjectId = task.subjectId,
                   isEditing = true
               )
           }
       }
    }

    fun onEvent(event: TaskAddEditEvent){
        when(event){
            is TaskAddEditEvent.TitleChanged -> {
                _uiState.value = _uiState.value.copy(title = event.value)
                predictionJob?.cancel()
                predictionJob = viewModelScope.launch {
                    delay(500)  // Wait 500ms for user to stop typing
                    predictPriority()
                }
            }
            is TaskAddEditEvent.DescriptionChanged -> {
                _uiState.value = _uiState.value.copy(description = event.value)
                predictPriority()
            }
            is TaskAddEditEvent.DueDateChanged -> {
                _uiState.value = _uiState.value.copy(dueDate = event.value )
                predictPriority()
            }
            is TaskAddEditEvent.CompletionToggled -> {
                _uiState.value = _uiState.value.copy(isCompleted = event.value)
            }
           is TaskAddEditEvent.SubjectChanged ->{
               _uiState.value =_uiState.value.copy(
                   subjectId = event.subjectId, subjectName = event.subjectName
               )
               predictPriority()
           }
           is TaskAddEditEvent.PriorityFeedback -> {
               handlePriorityFeedback(event.priorityLevel)
           }
            is TaskAddEditEvent.PriorityChanged ->{
                android.util.Log.d("PRIORITY_EVENT","Changed to ${event.value}")
                android.util.Log.d("ML_DEBUG","Clicked${event.value}")
                _uiState.value = _uiState.value.copy(
                    priority = event.value,
                    isPriorityOverridden = true
                )
                android.util.Log.d("ML_DEBUG","State Priority = ${_uiState.value.priority}")
            }

            TaskAddEditEvent.SaveTask ->
                saveTask()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun saveTask(){
        viewModelScope.launch {
             val state = _uiState.value
            val error = validate(state)
            if (error != null){
                _uiEvent.emit(UiEvent.ShowError(error))
                return@launch
        }
            _uiState.update{it.copy(isSaving = true)}
            try {
                Log.d("SAVE_TASK",
                    "priority=${state.priority},predicted=${state.predictedPriority}")
                val entity = TaskEntity(
                    id = if (state.isEditing)
                    requireNotNull(taskId) else 0,
                    subjectId = state.subjectId!!,
                    title = state.title.trim(),
                    dueDate = state.dueDate,
                    description = state.description,
                    isCompleted = state.isCompleted,
                    priority = state.priority
                )
                val savedId: Long = if (state.isEditing) {
                    repository.updateTask(entity)
                    entity.id
                } else {
                    repository.insertTask(entity)
                }

                val taskWithId = entity.copy(id = savedId)

                android.util.Log.d("ML_DEBUG","UI PRIORITY = ${state.priority}")

                val priorityLevel = when(state.priority){
                    "Low" -> 0
                    "Medium" -> 1
                    "High" -> 2
                    "Urgent" -> 3
                    else -> 1
                }
                android.util.Log.d("ML_DEBUG","Converted priority = $priorityLevel")

                val allTasks = repository.getAllTasks().first()

                mlHelper.feedbackTask(
                    task = taskWithId,
                    actualPriority = priorityLevel,
                    allTasks = allTasks
                )

                if (!taskWithId.isCompleted && taskWithId.dueDate != null){
                    reminderSchedule.scheduleTaskReminder(taskWithId)
                }

                _uiEvent.emit(UiEvent.SaveSuccess)

            } catch (e: Exception){
                _uiEvent.emit(UiEvent.ShowError("Failed to save task"))
            }finally {
                _uiState.update { it.copy(isSaving = false) }
            }
 }
    }
    private fun handlePriorityFeedback(priorityLevel: Int) {
        viewModelScope.launch {

            val state = _uiState.value

            if (state.subjectId == null || state.title.isBlank()) {
                return@launch
            }

            val allTasks = repository.getAllTasks().first()

            val tempTask = TaskEntity(
                id = taskId ?: 0,
                subjectId = state.subjectId,
                title = state.title,
                description = state.description,
                dueDate = state.dueDate,
                isCompleted = state.isCompleted,
                createdAt = System.currentTimeMillis()
            )

            mlHelper.feedbackTask(
                task = tempTask,
                actualPriority = priorityLevel,
                allTasks = allTasks
            )
        }
    }
    private fun predictPriority() {
        viewModelScope.launch {

            val state = _uiState.value

            _uiState.update {
                it.copy(isPredictionLoading = true)
            }

            if (state.title.isBlank() || state.subjectId == null) {
                _uiState.update {
                    it.copy(
                        predictedPriority = "Medium",
                        predictionConfidence = 0.3f,
                        isPredictionLoading = false
                    )
                }
                return@launch
            }

            try {
                val allTasks = repository.getAllTasks().first()

                val tempTask = TaskEntity(
                    id = 0,
                    subjectId = state.subjectId,
                    title = state.title,
                    description = state.description,
                    dueDate = state.dueDate
                )

                val result = withContext(Dispatchers.Default) {

                    val prediction =
                        mlHelper.predictTaskPriority(tempTask, allTasks)

                    val confidence =
                        mlHelper.getPredictionConfidence(tempTask, allTasks)

                    prediction to confidence
                }

                _uiState.update {
                    it.copy(
                        predictedPriority = result.first.label,
                        predictionConfidence = result.second,
                        isPredictionLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isPredictionLoading = false
                    )
                }
            }
        }
    }
}
