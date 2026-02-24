package com.example.todoappv2.task.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        .padding(8.dp)
        .clickable{onEdit()},
        elevation = CardDefaults
            .cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            TaskCompletionCheckBox(
                isCompleted = task.isCompleted,
                onCheckedChange = onCheckedChange,
                taskTitle = task.title
            )
            if (!task.description.isNullOrEmpty()){
                Text(text = task.description,
                    modifier= Modifier.padding(4.dp))
            }
        }
    }
}