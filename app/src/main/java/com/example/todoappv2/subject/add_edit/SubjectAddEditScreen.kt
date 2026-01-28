package com.example.todoappv2.subject.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun SubjectAddEditScreen(
    viewModel: SubjectAddEditViewModel
){
    val state = viewModel.uiState.collectAsState().value
    Column() {
        TextField(
            value = state.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Subject title") }
        )
        Button(
            onClick = viewModel::onSave,
            enabled = !state.isSaving
        ) {
            Text(if (state.isEditMode)"update else " else "Create")
        }
        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}