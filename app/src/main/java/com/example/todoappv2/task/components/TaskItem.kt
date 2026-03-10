@file:JvmName("TaskItemKt")

package com.example.todoappv2.task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleCompleted: (Boolean) -> Unit,
    onClick: () -> Unit
){
    val statusColor = when{
        task.isCompleted -> Color.Gray
        task.dueDate?.let {
          it <  System.currentTimeMillis()
        } == true -> Color(0xffd32f2f)
        else -> Color.Gray
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onToggleCompleted,
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color(0xff4a90e2)
                )
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        textDecoration = if (task.isCompleted)
                            TextDecoration.LineThrough else
                            TextDecoration.None,
                        color = if (task.isCompleted) Color.Gray else Color.Unspecified
                    )
                )
                Text(
                    text = task.description ?: "No description",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = statusColor
                    )
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 56.dp),
            thickness = 0.5.dp,
            color = Color.LightGray.copy(0.5f)

        )
    }
}