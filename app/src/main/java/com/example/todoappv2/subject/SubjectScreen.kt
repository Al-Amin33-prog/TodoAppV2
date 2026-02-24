package com.example.todoappv2.subject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.todoappv2.subject.components.EmptySubjectState
import com.example.todoappv2.subject.components.SubjectList
import com.example.todoappv2.subject.components.SubjectProgressBar

@Composable
fun SubjectScreen(
    viewModel: SubjectViewModel,
    onAddSubject: ()-> Unit,
    onOpenSubject: (Long) -> Unit,
    onEditSubject: (Long) -> Unit
){
    val state = viewModel.uiState.collectAsState().value
    when{
        state.isLoading ->{
            SubjectProgressBar()
        }
        state.subjects.isEmpty() ->{
            EmptySubjectState()
        }
        else ->{
            SubjectList(
                subjects = state.subjects,
                onDelete = { subject ->
                    viewModel.onEvent(
                        SubjectEvent.DeleteSubject(subject)
                    )

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