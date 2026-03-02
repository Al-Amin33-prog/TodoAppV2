package com.example.todoappv2.subject.add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SubjectAddEditScreen(
    viewModel: SubjectAddEditViewModel,
    onDone: () -> Unit
){
    LaunchedEffect(Unit) {
        viewModel.subjectAddEditEvent.collect { event ->
            when(event){
                is SubjectAddEditEvent.Success ->
                    onDone()
            }
        }
    }
    val state = viewModel.uiState.collectAsState().value
    Column {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            Text(
                text = if (
                    state.isEditMode
                )"Edit Subject" else "Add Subject",style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = {Text("Enter subject title")},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Text(
                "Choose Color", style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val colors = listOf(
                    0xFFF44336L,
                    0xFFE91E63L,
                    0xFF9C27B0L,
                    0xFF4CAF50L,
                    0xFFFF9800L
                )
                colors.forEach { colorHex ->
                    val isSelected = state.colorHex == colorHex
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(colorHex))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .clickable{
                                viewModel.onColorChanged(colorHex)
                            }
                    )

                }
            }
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )

            }

            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                viewModel.onSave()
            },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = !state.isSaving,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    if (state.isEditMode)"Update" else "Create"
                )
            }
        }
    }
}