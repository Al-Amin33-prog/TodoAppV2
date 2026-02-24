package com.example.todoappv2.task.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskList(
    tasks: List<TaskEntity>,
    onToggleCompleted: (TaskEntity, Boolean) -> Unit,
    onDelete: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit
){
    LazyColumn {
        items(
            items = tasks,
            key = {it.id}
        ){task ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {value->
                    if (value == SwipeToDismissBoxValue.EndToStart){
                        onDelete(task)
                        true
                    }else{
                        false
                    }

            }
            )
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    DeleteBackground()
                },
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true


            ) {
                TaskItem(
                    task = task,
                    onCheckedChange = {completed ->
                        onToggleCompleted(task, completed)
                    },
                    onEdit = {
                        onEditTask(task)
                    },

                )
            }

        }
    }
}


