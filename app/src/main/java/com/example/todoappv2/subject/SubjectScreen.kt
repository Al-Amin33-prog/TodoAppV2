package com.example.todoappv2.subject

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.todoappv2.subject.components.EmptySubjectState
import com.example.todoappv2.subject.components.SubjectList
import com.example.todoappv2.subject.components.SubjectProgressBar
import kotlinx.coroutines.launch

@Composable
fun SubjectScreen(
    viewModel: SubjectViewModel,
    onAddSubject: ()-> Unit,
    onOpenSubject: (Long) -> Unit,
    onEditSubject: (Long) -> Unit
){
    val state = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {padding ->
        when{
            state.isLoading ->{
                SubjectProgressBar()
            }

            state.subjects.isEmpty() ->{
                EmptySubjectState(
                    onAddSubject =  onAddSubject
                )
            }
            else ->{
                SubjectList(
                    subjects = state.subjects,
                    onDelete = { subject ->
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = " Delete${subject.name}?",
                                actionLabel = "Confirm",
                                withDismissAction = true
                            )
                            if (result == SnackbarResult.ActionPerformed){
                                viewModel.onEvent(
                                    SubjectEvent.DeleteSubject(subject)
                                )
                            }
                        }

                    },
                    onSubjectClick = {subject ->
                        onOpenSubject(subject.id)
                    },
                    onAddSubject = onAddSubject,
                    onEditSubject = {subject ->
                        onEditSubject(subject.id)
                    }
                )
            }
        }
    }

}