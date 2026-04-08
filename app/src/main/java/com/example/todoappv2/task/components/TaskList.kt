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
    groupedTasks: Map<TaskSection, List<TaskEntity>>,
    onToggleCompleted: (TaskEntity) -> Unit,
    onDelete: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    taskBeingDeleted: TaskEntity?,
    isSelectionMode: Boolean,
    selectedTaskIds: Set<Long>,
    onToggleSelectionMode: () -> Unit,
    onToggleSelection: (Long) -> Unit
){
    LazyColumn {
        groupedTasks.forEach { (section, tasks) ->
            item {
                TaskSectionHeader(
                    title = when(section){
                        TaskSection.Today -> "Today"
                        TaskSection.Overdue -> "Overdue"
                        TaskSection.Upcoming -> "Upcoming"
                        TaskSection.NoDate -> "No Date"
                    }
                )
            }

            items(
                items = tasks.filter { it.id !=taskBeingDeleted?.id },
                key = {it.id}
            ){task ->
               val dismissSate = rememberSwipeToDismissBoxState(
                   confirmValueChange = { value ->
                       if (value == SwipeToDismissBoxValue.EndToStart){
                           onDelete(task)
                           true
                       } else false

                   }
               )
                if (!isSelectionMode){
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
                            onEdit = {onEditTask(task)},
                            isSelected = selectedTaskIds.contains(task.id),
                            isSelectionMode = isSelectionMode,
                            onLongPress = {
                                onToggleSelectionMode()
                                onToggleSelection(task.id)
                            },
                            onSelect = {
                                onToggleSelection(task.id)
                            },


                            )
                    }
                }



            }

        }
         }
    }



