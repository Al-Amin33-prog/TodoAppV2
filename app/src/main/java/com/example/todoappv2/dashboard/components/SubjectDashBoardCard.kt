package com.example.todoappv2.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.dashboard.SubjectWithStats

@Composable
fun SubjectDashBoardCard(
    data: SubjectWithStats,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{onClick()}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = data.subject.name)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Total: ${data.totalTasks}")
            Text(text = "Completed: ${data.completedTasks}")
            Text(text = "Pending:${data.pendingTasks}")
        }
    }
}