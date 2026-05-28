package com.example.todoappv2.task.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.todoappv2.task.TaskUiModel

@Composable
fun TaskList(
    groupedTasks: Map<TaskSection, List<TaskUiModel>>,
    onToggleCompleted: (TaskUiModel) -> Unit,
    onDelete: (TaskUiModel) -> Unit,
    onEditTask: (TaskUiModel) -> Unit,
    taskBeingDeletedId: Long?,
    isSelectionMode: Boolean,
    selectedTaskIds: Set<Long>,
    onStartSelection: (Long) -> Unit,
    onToggleSelection: (Long) -> Unit
) {

    val scope = rememberCoroutineScope()

    LazyColumn {
        groupedTasks.forEach { (section, tasks) ->
            item {
                TaskSectionHeader(
                    title = when (section) {
                        TaskSection.Today -> "Today"
                        TaskSection.Overdue -> "Overdue"
                        TaskSection.Upcoming -> "Upcoming"
                        TaskSection.NoDate -> "No Date"
                    }
                )
            }

            items(
                items = tasks.filter { it.id != taskBeingDeletedId },//here
                key = { it.id }
            ) { task ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        when (value) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                onDelete(task)
                                true
                            }
                            SwipeToDismissBoxValue.StartToEnd -> {
                                onEditTask(task)

                                false
                            }
                            else -> false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = !isSelectionMode,
                    enableDismissFromEndToStart = !isSelectionMode,
                    backgroundContent = {
                        when (dismissState.dismissDirection) {
                            SwipeToDismissBoxValue.StartToEnd -> EditBackground(dismissState)
                            SwipeToDismissBoxValue.EndToStart -> DeleteBackground(dismissState)
                            else -> {}
                        }
                    }
                ) {
                    TaskItem(
                        task = task,
                        onToggleCompleted = { onToggleCompleted(task) },
                        onEdit = { onEditTask(task) },
                        isSelected = selectedTaskIds.contains(task.id),
                        isSelectionMode = isSelectionMode,
                        onLongPress = { onStartSelection(task.id) },
                        onSelect = { onToggleSelection(task.id) }
                    )
                }
            }
        }
    }
}
