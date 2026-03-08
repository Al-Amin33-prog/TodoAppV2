package com.example.todoappv2.subject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubjectViewModel (
    private val repository: AppRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(SubjectUiState(isLoading = true))
    val uiState: StateFlow<SubjectUiState> = _uiState.asStateFlow()
    private var allSubjects: List<SubjectEntity> = emptyList()
    init {
        observeSubjects()
    }
    private fun observeSubjects(){
       viewModelScope.launch {
           repository.getSubjects().collect { subjects ->
               allSubjects = subjects
               _uiState.update {
                   it.copy(
                       isLoading = false,
                       subjects = subjects
                   )
               }

           }
       }
    }
    private fun filterSubjects(){
        val query = _uiState.value.searchQuery.lowercase()
        val filtered = if (query.isBlank()){
            allSubjects
        }
        else{
            allSubjects.filter{subject ->
                subject.name.lowercase().contains(query)

            }
        }
        _uiState.update {
            it.copy(subjects = filtered)
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
            is SubjectEvent.SearchQueryChange -> {
                _uiState.update { it.copy(
                    searchQuery = event.query
                )
                }
                filterSubjects()
            }
        }
    }
}
