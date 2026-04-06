@file:JvmName("TaskItemKt")

package com.example.todoappv2.task.components


import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.todoappv2.core.util.formatDueDateLabel
import com.example.todoappv2.data.local.entity.TaskEntity

@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleCompleted: (Boolean) -> Unit,
    onEdit: () -> Unit,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onLongPress: () -> Unit,
    onSelect: () -> Unit

){

    val statusColor = when{
        task.isCompleted -> Color.Gray
        task.dueDate?.let {
          it <  System.currentTimeMillis()
        } == true -> Color(0xffd32f2f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val dueLabel = formatDueDateLabel(task.dueDate)
        Row(
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        if (isSelectionMode) onSelect()
                        else onEdit()
                    },
                    onLongClick = onLongPress

                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelectionMode){
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {onSelect()}
                )
            }else
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = {onToggleCompleted(it)},
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
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        )  else MaterialTheme.colorScheme.onSurface
                    )
                )
                if (!task.description.isNullOrBlank()){
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = statusColor
                        )
                    )
                }
            }
            val bargeColor = when(dueLabel){
                "Overdue" -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                "Today" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val textColor = when(dueLabel) {
                "Overdue" -> MaterialTheme.colorScheme.error
                "Today" -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            if (dueLabel != null){
                Text(
                    text = dueLabel,
                    modifier = Modifier.background(
                        color = bargeColor,
                        shape = RoundedCornerShape(50.dp)
                    )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor

                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 56.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant

        )
    }
}