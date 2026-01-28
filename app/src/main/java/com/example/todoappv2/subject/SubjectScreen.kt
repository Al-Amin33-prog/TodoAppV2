package com.example.todoappv2.subject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.todoappv2.subject.components.EmptySubjectState
import com.example.todoappv2.subject.components.SubjectList
import com.example.todoappv2.subject.components.SubjectProgressBar

@Composable
fun SubjectScreen(
    viewModel: SubjectViewModel
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

                }
            )
        }
    }
}