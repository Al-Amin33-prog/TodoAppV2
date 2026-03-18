package com.example.todoappv2.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class StatisticsViewModel (
    private val  repository: AppRepository
): ViewModel() {
    private  val _uiState = MutableStateFlow(StatisticsState())
    val uiState: StateFlow<StatisticsState> = _uiState.asStateFlow()
    init {
        observeTasks()
    }
    private fun observeTasks(){
        viewModelScope.launch {
            repository.getAllTasks().collect { tasks ->
                val stats = calculateStats(tasks)
                _uiState.value = stats

            }
        }
    }
    private fun calculateStats(tasks: List<TaskEntity>): StatisticsState{
        val now = System.currentTimeMillis()
        val weekAgo = now - (7 * 24 * 60 * 60 * 1000)
        val weeklyTasks = tasks.filter {
            it.createdAt >= weekAgo
        }
        val total = weeklyTasks.size
        val completed = weeklyTasks.count{it.isCompleted}
        val pending = weeklyTasks.count { !it.isCompleted }
        val overdue = weeklyTasks.count { !it.isCompleted && it.dueDate != null && it.dueDate <  now }
        val productivity = if (total == 0) 0 else(completed * 100)/ total
        return StatisticsState(
            productivity = productivity,
            totalTasks = total,
            completedTask = completed,
            pendingTasks = pending,
            overdueTasks = overdue,
            weeklyCompleted = calculateWeeklyCompletion(weeklyTasks)
        )
    }
    private fun calculateWeeklyCompletion(
        tasks: List<TaskEntity>
    ): List<Int>{
        val calendar = Calendar.getInstance()
        val result = MutableList(7){0}
        tasks.filter { it.isCompleted }.forEach { task ->
            calendar.timeInMillis = task.createdAt
            val day = calendar.get(Calendar.DAY_OF_WEEK)
            val index = when(day){
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                else -> 6
            }
            result[index]++
        }
        return result

    }


}