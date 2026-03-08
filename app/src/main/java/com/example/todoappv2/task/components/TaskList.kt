package com.example.todoappv2.task.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskList(
    groupedTasks: Map<TaskSection, List<TaskEntity>>,
    onToggleCompleted: (TaskEntity, Boolean) -> Unit,
    onDelete: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit
){
    LazyColumn {
        groupedTasks.forEach { (section , tasks) ->
            item {
                TaskSectionHeader(section.title)
            }
            items(
                items = tasks,
                key = {it.id}
            ){task ->
               TaskCard(
                  task = task,
                   onDelete = {
                       onDelete(task)
                   }
               )

            }

        }
         }
    }



