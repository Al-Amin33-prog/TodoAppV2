package com.example.todoappv2.subject.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmptySubjectState(
    onAddSubject: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "No Subjects yet",
           style =  MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Start by adding your first subject",
            style  = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onAddSubject
        ) {
            Text("Add Subject", style = MaterialTheme.typography.labelMedium)
        }
    }


}