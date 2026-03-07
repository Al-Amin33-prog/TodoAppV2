package com.example.todoappv2.task.add_edit

sealed class TaskAddEditMode {
    data class Add(
        val subjectId: Long? = null
    ): TaskAddEditMode()
    data class Edit(
        val taskId: Long
    ): TaskAddEditMode()
}