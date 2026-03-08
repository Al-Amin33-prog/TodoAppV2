package com.example.todoappv2.task.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card

import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskItem(
    task: TaskEntity,
    onCheckedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,


){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp,8.dp)
       ) {
        Row(modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
               Text(
                   text = task.title,
                   style = MaterialTheme.typography.titleMedium
               )
                task.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
                Text(
                    text = if (task.isCompleted)"Completed" else "Pending",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (task.isCompleted) Color(0xff2e7d32) else Color.Gray
                )

            }
        }
    }
}