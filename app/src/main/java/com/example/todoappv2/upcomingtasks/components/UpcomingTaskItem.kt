package com.example.todoappv2.upcomingtasks.components

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.local.entity.TaskEntity
import java.util.Locale

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

        MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            task.dueDate?.let {
                Spacer(modifier =Modifier.height(4.dp))
                Text(
                    text = SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault())
                        .format(it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}