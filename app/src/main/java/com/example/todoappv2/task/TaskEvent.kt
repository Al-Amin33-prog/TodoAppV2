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
    data class DeleteTask(val task: TaskEntity): TaskEvent()
    data class UpdateTask(val task: TaskEntity): TaskEvent()
    data class ChangeFilter(val filter: TaskFilterType): TaskEvent()
}
