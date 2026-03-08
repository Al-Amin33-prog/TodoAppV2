package com.example.todoappv2.subject


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoappv2.subject.components.EmptySubjectState
import com.example.todoappv2.subject.components.SubjectList
import com.example.todoappv2.subject.components.SubjectProgressBar
import kotlinx.coroutines.launch
import com.example.todoappv2.R

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
                Column(
                    modifier = Modifier.padding(padding)
                ){
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = {
                            viewModel.onEvent(
                                SubjectEvent.SearchQueryChange(it)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        placeholder = { Text("search subjects") },
                        leadingIcon = {
                            Icon(
                           painter = painterResource(R.drawable.search_24px__1_),  contentDescription = null
                            )
                        },
                        singleLine = true
                    )
                    SubjectList(
                        subjects = state.subjects,
                        onDelete = { subject ->
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Delete${subject.name}?",
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

}