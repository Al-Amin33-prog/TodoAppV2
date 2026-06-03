package com.example.todoappv2.ml.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriorityOverrideSection(
    selectedPriority: String,
    onPriorityChange: (String) -> Unit,
    mlPredictedPriority: String,
    taskId: Long,
   onPriorityOverride:(Int) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Task Priority", style = MaterialTheme.typography.titleMedium)


            Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
          //  horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf("Low", "Medium", "High", "Urgent").forEach { priority ->
                Log.d("PRIORITY RENDER","selected = $selectedPriority chip =$priority")
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = {
                        Log.d("ML_CLICK","Chip Clicked = $priority")
                        onPriorityChange(priority)

                    },
                    label = { Text(priority) },
                    modifier = Modifier

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