package com.example.todoappv2.task

import com.example.todoappv2.data.local.entity.TaskEntity

import com.example.todoappv2.task.components.TaskFilterType

sealed class TaskEvent {
    data class AddTask(
        val subjectId: Long,
        val title: String,
        val description: String?,
        val dueDate: Long?
    ): TaskEvent()
    data class DeleteTask(val taskId: Long): TaskEvent()
    data class UpdateTask(val taskId: Long): TaskEvent()
    data class ChangeFilter(val filter: TaskFilterType): TaskEvent()
    data class SearchTasks(val query: String): TaskEvent()
    data class ToggleTaskCompletion(val taskId: Long): TaskEvent()
    data class RestoreTask(val task: TaskEntity): TaskEvent()

    data class ToggleTaskSelection(val taskId: Long): TaskEvent()
    object DeleteSelectedTasks : TaskEvent()
    object ClearSelection: TaskEvent()
    object SelectAll: TaskEvent()
    data class StartSelection(val taskId: Long): TaskEvent()
}
