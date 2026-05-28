package com.example.todoappv2.ml.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.task.TaskViewModel


@Composable
fun PriorityOverrideSection(
    selectedPriority: String,
    onPriorityChange: (String) -> Unit,
    mlPredictedPriority: String,
    taskId: Long,
    viewModel: TaskViewModel  // Add this
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Task Priority", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Low", "Medium", "High", "Urgent").forEach { priority ->
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = {
                        onPriorityChange(priority)
                        // CALL THIS WHEN USER MANUALLY SETS PRIORITY
                        if (selectedPriority != mlPredictedPriority) {
                            val priorityLevel = when (priority) {
                                "Low" -> 0
                                "Medium" -> 1
                                "High" -> 2
                                "Urgent" -> 3
                                else -> 1
                            }
                            viewModel.onUserSetPriority(taskId, priorityLevel)
                        }
                    },
                    label = { Text(priority) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }

        // Show if user overrode ML prediction
        if (selectedPriority != mlPredictedPriority) {
            Text(
                "✏️ You overrode the AI suggestion ($mlPredictedPriority → $selectedPriority)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}