package com.example.todoappv2.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.local.entity.TaskEntity
import java.text.DateFormat

@Composable
fun UpcomingTaskItem(
    task: TaskEntity,
    onClick: (Long) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable{onClick(task.id)},

        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            task.dueDate?.let {
                Text(
                    text = "Due: ${DateFormat.getDateInstance().format(it)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}