package com.example.todoappv2.task.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskFilter(
    selectedFilter: TaskFilterType,
    onFilterSelected: (TaskFilterType) -> Unit
){
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        FilterChip(selected = selectedFilter
        is TaskFilterType.All,
            onClick = {
                onFilterSelected(TaskFilterType.All)
            },
            label = { Text("All") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = selectedFilter
            is TaskFilterType.Pending,
            onClick = {
                onFilterSelected(TaskFilterType.Pending)
            },
            label = {Text("Pending")}
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = selectedFilter
            is TaskFilterType.Completed,
            onClick = {
                onFilterSelected(TaskFilterType.Completed)
            },
            label = {Text("Completed")}
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = selectedFilter
            is
            TaskFilterType.Overdue,
            onClick = {
                onFilterSelected(TaskFilterType.Overdue)
            },
            label = {Text("Overdue")}
        )
    }

}