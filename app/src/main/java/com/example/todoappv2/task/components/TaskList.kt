package com.example.todoappv2.task.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskList(
    groupedTasks: Map<TaskSection, List<TaskEntity>>,
    onToggleCompleted: (TaskEntity) -> Unit,
    onDelete: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    taskBeingDeleted: TaskEntity?
){
    LazyColumn {
        groupedTasks.forEach { (_, tasks) ->

            items(
                items = tasks,
                key = {it.id}
            ){task ->
                val dismissSate = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart){
                            onDelete(task)
                            false
                        }else{
                            false
                        }
                    }
                )
                LaunchedEffect(dismissSate.currentValue) {
                    if (
                        dismissSate.currentValue != SwipeToDismissBoxValue.Settled
                        ){
                        dismissSate.reset()
                    }
                }
                SwipeToDismissBox(
                    state = dismissSate,
                    backgroundContent = {
                        DeleteBackground(dismissState = dismissSate)
                    }
                ) {
                    TaskItem(
                        task = task,
                        onToggleCompleted = {
                            onToggleCompleted(task)
                        },
                        onClick = {
                            onEditTask(task)
                        }
                    )
                }

            }

        }
         }
    }



