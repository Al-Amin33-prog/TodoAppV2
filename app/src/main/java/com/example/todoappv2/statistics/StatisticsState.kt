package com.example.todoappv2.statistics

data class StatisticsState(
    val productivity: Int = 0,
    val totalTasks: Int = 0,
    val completedTask: Int = 0,
    val pendingTasks: Int = 0,
    val overdueTasks: Int = 0,

    val weeklyCompleted: List<Int> = List(7){0},
    val isLoading: Boolean = true

)