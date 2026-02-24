package com.example.todoappv2.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.dashboard.SubjectDashBoardCard
import com.example.todoappv2.dashboard.SubjectWithStats

@Composable
fun PendingTaskSummary(
    subjects: List<SubjectWithStats>,
    onSubjectClick:(Long)-> Unit,
){
    Column {
        Text(
            "Subjects",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        subjects.forEach { subjectStats ->
            SubjectDashBoardCard(
               data = subjectStats,
                onClick = {
                    onSubjectClick(subjectStats.subject.id)
                }

                )
        }
    }
}