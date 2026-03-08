package com.example.todoappv2.task.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskCompletionCheckBox(
    isCompleted: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    taskTitle: String
){
    Row(modifier = Modifier.padding(8.dp)) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = onCheckedChange
        )
        Text(
            text= taskTitle,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}