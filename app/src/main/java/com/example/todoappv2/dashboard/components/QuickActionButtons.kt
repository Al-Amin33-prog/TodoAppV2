package com.example.todoappv2.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionButtons(
    onAddTask: () -> Unit ,
    onAddSubject: () -> Unit
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onAddTask,
            modifier = Modifier.weight(1f)) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onAddSubject,
            modifier = Modifier.weight(1f)) {
            Text("Add Subject")
        }
    }
}