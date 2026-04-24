package com.example.todoappv2.task



class TaskUiModel (

    val id: Long,
    val subjectId: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val dueDate: Long?,
    val dueLabel: String?,
    val isOverDue: Boolean,
    val createdAt: Long
)
