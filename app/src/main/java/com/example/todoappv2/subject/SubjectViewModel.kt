package com.example.todoappv2.subject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubjectViewModel (
    private val repository: AppRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(SubjectUiState(isLoading = true))
    val uiState: StateFlow<SubjectUiState> = _uiState.asStateFlow()
    init {
        observeSubjects()
    }
    private fun observeSubjects(){
       viewModelScope.launch {
           repository.getSubjects().collect { subjects ->
               _uiState.value = _uiState.value.copy(
                   isLoading = false,
                   subjects = subjects,
                   error = null
               )

           }
       }
    }
    fun addSubject(subject: SubjectEntity){
        viewModelScope.launch {
            repository.insertSubject(subject)
        }
    }
    fun deleteSubject(subject: SubjectEntity){
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }
    fun updateSubject(subject: SubjectEntity){
        viewModelScope.launch {
            repository.updateSubject(subject)
        }
    }
    fun onEvent(event: SubjectEvent){
        when(event){
            is SubjectEvent.AddSubject -> {
                addSubject(
                    SubjectEntity(
                        name = event.name,
                        colorHex = 0xFF2563EB,
                        createdAt = System.currentTimeMillis()
                    )
                )
            }
            is SubjectEvent.DeleteSubject -> {
                deleteSubject(event.subject)
            }
            is SubjectEvent.UpdateSubject -> {
                updateSubject(event.subject)
            }
        }
    }
}
