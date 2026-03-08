package com.example.todoappv2.task.components

sealed class TaskSection (
    val title: String
){
    object Overdue : TaskSection("Overdue")
    object Today: TaskSection("Today")
    object Upcoming: TaskSection("Upcoming")
    object NoDate: TaskSection("No Date")
}