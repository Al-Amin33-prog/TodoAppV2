package com.example.todoappv2.task.add_edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoappv2.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddEditScreen(
    viewModel: TaskAddEditViewModel,
    onDone: () -> Unit,
    onCancel: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val subjects by viewModel.subjects.collectAsState(initial = emptyList())
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }


  LaunchedEffect(state.dueDate) {
      state.dueDate?.let {
          calendar.timeInMillis = it
      }
  }

    var subjectExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TaskAddEditViewModel.UiEvent.SaveSuccess -> onDone()
                is TaskAddEditViewModel.UiEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Task Title",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onEvent(TaskAddEditEvent.TitleChanged(it)) },
                placeholder = {
                    Text(
                        text = "Enter task title",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Description (optional)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(TaskAddEditEvent.DescriptionChanged(it)) },
                placeholder = {
                    Text(
                        text = "Enter description",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Subject",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = subjectExpanded,
                onExpandedChange = { subjectExpanded = it }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = state.subjectName,
                    onValueChange = {},
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_book),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.keyboard_arrow_down_24px),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                ExposedDropdownMenu(
                    expanded = subjectExpanded,
                    onDismissRequest = { subjectExpanded = false }
                ) {
                    subjects.forEach { subject ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = subject.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                viewModel.onEvent(TaskAddEditEvent.SubjectChanged(subject.id, subject.name))
                                subjectExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Due Date (optional)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.dueDate?.let {
                        SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault()).format(it)
                    } ?: "Select Date & Time",
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    painter = painterResource(R.drawable.keyboard_arrow_down_24px),
                    contentDescription = null
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Priority",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = it }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = state.priority,
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.keyboard_arrow_down_24px),
                            contentDescription = null
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    listOf("Low", "Medium", "High").forEach { priority ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = priority,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                viewModel.onEvent(TaskAddEditEvent.PriorityChanged(priority))
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Cancel", style = MaterialTheme.typography.labelLarge)
                }
                Button(
                    enabled = !state.isSaving,
                    onClick = { viewModel.onEvent(TaskAddEditEvent.SaveTask) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Save", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
    LaunchedEffect(showDatePicker) {
        if (showDatePicker) {
           val dialog =  DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    showDatePicker = false
                    showTimePicker = true
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.setOnDismissListener {
                showDatePicker = false

            }
            dialog.show()
        }
    }

   LaunchedEffect(showTimePicker) {
       if (showTimePicker) {
          val dialog =  TimePickerDialog(
               context,
               { _, hour, minute ->
                   calendar.set(Calendar.HOUR_OF_DAY, hour)
                   calendar.set(Calendar.MINUTE, minute)
                   viewModel.onEvent(TaskAddEditEvent.DueDateChanged(calendar.timeInMillis)
                   )
                   showTimePicker = false
               },
               calendar.get(Calendar.HOUR_OF_DAY),
               calendar.get(Calendar.MINUTE),
               false
           )
           dialog.setOnDismissListener {
               showTimePicker = false
           }
           dialog.show()
       }
   }


}
