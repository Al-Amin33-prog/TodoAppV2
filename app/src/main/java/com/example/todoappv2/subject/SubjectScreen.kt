package com.example.todoappv2.subject

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.todoappv2.subject.components.EmptySubjectState
import com.example.todoappv2.subject.components.SubjectList
import com.example.todoappv2.subject.components.SubjectProgressBar
import kotlinx.coroutines.launch
import com.example.todoappv2.R
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.subject.components.SubjectTopBar
import com.example.todoappv2.task.TaskEvent

@Composable
fun SubjectScreen(
    viewModel: SubjectViewModel,
    onAddSubject: () -> Unit,
    onOpenSubject: (Long) -> Unit,
    onEditSubject: (Long) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var recentlyDeletedSubject by remember { mutableStateOf<SubjectEntity?>(null) }
    LaunchedEffect(recentlyDeletedSubject) {
        recentlyDeletedSubject?.let { subject ->
            val result = snackbarHostState.showSnackbar(
                message = "Subject deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onEvent(SubjectEvent.RestoreSubject(subject))
            }
            recentlyDeletedSubject = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Unified Selection Bar (Overlay to avoid "Double Bar" visual)
            AnimatedVisibility(
                visible = state.isSelectionMode,
                enter = expandVertically(),
                exit = shrinkVertically(),
                modifier = Modifier.zIndex(2f)
            ) {
                SubjectTopBar(
                    isSelectionMode = state.isSelectionMode,
                    selectedCount = state.selectedSubjectIds.size,
                    isAllSelected = state.selectedSubjectIds.size == state.subjects.size && state.subjects.isNotEmpty(),
                    onClearSelection = { viewModel.onEvent(SubjectEvent.ClearSelection) },
                    onSelectAll = { viewModel.onEvent(SubjectEvent.SelectAllSubjects) },
                    onDeleteSelected = { viewModel.onEvent(SubjectEvent.DeleteSelectedSubjects) },
                    onDeselectedSubject = {viewModel.onEvent(SubjectEvent.DeselectAllSubjects)}
                )
            }

            // 2. Main Content
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                OutlinedTextField(
                    value = state.searchQuery,
                    shape = RoundedCornerShape(16.dp),
                    onValueChange = { viewModel.onEvent(SubjectEvent.SearchQueryChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_subjects),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.search_24px__1_),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true
                )

                when {
                    state.isLoading -> {
                        SubjectProgressBar()
                    }
                    state.subjects.isEmpty() -> {
                        EmptySubjectState(onAddSubject = onAddSubject)
                    }
                    else -> {
                        SubjectList(
                            subjects = state.subjects,
                            onDelete = { subject ->

                                recentlyDeletedSubject = subject
                                viewModel.onEvent(SubjectEvent.DeleteSubject(subject))
                            },
                            onSubjectClick = { onOpenSubject(it.id) },
                            onEditSubject = { onEditSubject(it.id) },
                            onAddSubject = onAddSubject,
                            isSelectionMode = state.isSelectionMode,
                            selectedSubjectId = state.selectedSubjectIds,
                            onToggleSelectionMode = { viewModel.onEvent(SubjectEvent.ToggleSelectionMode) },
                            onToggleSelection = { id -> viewModel.onEvent(SubjectEvent.ToggleSubjectSelection(id)) }
                        )
                    }
                }
            }
        }
    }
}
