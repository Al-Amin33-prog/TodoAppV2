package com.example.todoappv2.task.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskList(
    tasks: List<TaskEntity>,
    onDelete: (TaskEntity) -> Unit
){
    LazyColumn{
        items(tasks){task ->
            TaskCard(
                task = task,
                onDelete = {onDelete(task)}
            )

        }
    }

}