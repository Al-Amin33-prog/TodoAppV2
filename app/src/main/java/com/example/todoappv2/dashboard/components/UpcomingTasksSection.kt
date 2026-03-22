package com.example.todoappv2.dashboard.components

import androidx.compose.runtime.Composable
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.upcomingtasks.components.EmptyUpcomingState
import com.example.todoappv2.upcomingtasks.components.UpcomingTaskItem

@Composable
 fun UpcomingTasksSection(
    tasks: List<TaskEntity>,
    onTaskClick: (Long) -> Unit
){
    if (tasks.isEmpty()){
        EmptyUpcomingState()
    }else{
        tasks.forEach { task ->
            UpcomingTaskItem(task, onTaskClick)
        }
    }
}