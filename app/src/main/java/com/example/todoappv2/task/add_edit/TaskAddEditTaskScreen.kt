package com.example.todoappv2.task.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun TaskAddEditScreen(
    viewModel: TaskAddEditViewModel,
    onDone: ()-> Unit
){
    val state = viewModel.uiState.collectAsState().value
    Column {
        TextField(
            value = state.title,
            onValueChange = {
                viewModel.onEvent(TaskAddEditEvent.TitleChanged(it))
            },
            label = { Text("Task Title") }
        )
        TextField(
            value = state.description,
            onValueChange = {
                viewModel.onEvent(TaskAddEditEvent.DescriptionChanged(it))
            },
            label = {Text("Description")}
        )
        Button(
            onClick = {
                viewModel.onEvent(TaskAddEditEvent.SaveTask)
                onDone()
            },
            enabled = !state.isSaving
        ) {
           Text(if (state.isEditing)"Update Task" else "Save Task" +
                   "")
        }
    }
}


