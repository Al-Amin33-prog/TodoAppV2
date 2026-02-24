package com.example.todoappv2.task.add_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.example.todoappv2.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddEditScreen(
    viewModel: TaskAddEditViewModel,
    //schedule: TaskReminderSchedule,
    onDone: ()-> Unit,
    onCancel: () -> Unit
){
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is TaskAddEditViewModel.UiEvent.SaveSuccess ->
                    onDone()
                is TaskAddEditViewModel.UiEvent.ShowError -> {

                }
            }
        }
    }
    val state = viewModel.uiState.collectAsState().value
    val subjects = viewModel.subjects.collectAsState(initial = emptyList()).value
    var subjectExpanded by remember{mutableStateOf(false)}
    var priorityExpanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = state.title,
            onValueChange = {
                viewModel.onEvent(TaskAddEditEvent.TitleChanged(it))
            },
            label = {Text("Task Title")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = state.description,
            onValueChange = {
                viewModel.onEvent(TaskAddEditEvent.DescriptionChanged(it))
            },
            label = {Text("Description(optional)")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        ExposedDropdownMenuBox(
          expanded = subjectExpanded,
            onExpandedChange = {subjectExpanded = it}
        ) {
            TextField(
                readOnly = true,
                value = state.subjectName,
                onValueChange = {},
                label = {Text("Subject")},
                trailingIcon = {
                   ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded)
                }, modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    )

            ExposedDropdownMenu(expanded = subjectExpanded,
                onDismissRequest = {subjectExpanded = false}) {
                subjects.forEach { subject->
                    DropdownMenuItem(
                        text = {Text(subject.name)},
                        onClick = {
                            viewModel.onEvent(
                                TaskAddEditEvent.SubjectChanged(
                                    subject.id,
                                    subject.name
                                )

                            )
                            subjectExpanded = false
                        }
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        ExposedDropdownMenuBox(
            expanded = priorityExpanded,
            onExpandedChange =  {priorityExpanded= it}
        ) {
            TextField(
                readOnly =  true,
                value = state.priority,
                onValueChange = {},
                label = {Text("Priority")},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded =  priorityExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = priorityExpanded,
                onDismissRequest = {priorityExpanded = false}) {
                listOf("Low", "Medium","High").forEach { priority ->
                    DropdownMenuItem(
                        text = {Text(priority)},
                        onClick = {
                            viewModel.onEvent(
                                TaskAddEditEvent.PriorityChanged(priority)
                            )
                            priorityExpanded = false

                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Button(onClick = {
                viewModel.onEvent(TaskAddEditEvent.SaveTask)

            }, enabled =  !state.isSaving) {
                Text(if (state.isEditing)"Update" else "Save")

            }
        }
    }


}


