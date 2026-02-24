package com.example.todoappv2.task.components

sealed class TaskFilterType{
    object All : TaskFilterType()
    object Completed: TaskFilterType()
    object Pending: TaskFilterType()
    data class BySubject(val subjectId:Long): TaskFilterType()
}
