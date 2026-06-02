package com.example.todoappv2.subject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
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
               refreshUiState()
           }
       }
    }
    
    private fun refreshUiState() {
        val query = _uiState.value.searchQuery.lowercase()
        val filtered = if (query.isBlank()){
            allSubjects
        } else {
            allSubjects.filter { it.name.lowercase().contains(query) }
        }
        
        _uiState.update {
            it.copy(
                isLoading = false,
                subjects = filtered
            )
        }
    }

    
    fun onEvent(event: SubjectEvent){
        when(event){
            is SubjectEvent.AddSubject -> {
                viewModelScope.launch {
                    repository.insertSubject(
                        SubjectEntity(
                            name = event.name,
                            colorHex = 0xFF2563EB,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                }
            }
            is SubjectEvent.DeleteSubject -> {
                viewModelScope.launch { repository.deleteSubject(event.subject) }
            }
            is SubjectEvent.UpdateSubject -> {
                viewModelScope.launch { repository.updateSubject(event.subject) }
            }
            is SubjectEvent.SearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                refreshUiState()
            }
            is SubjectEvent.ToggleSelectionMode -> {
                _uiState.update { it.copy(
                    isSelectionMode = !it.isSelectionMode,
                    selectedSubjectIds = emptySet()
                )}
            }
            is SubjectEvent.ToggleSubjectSelection -> {
                val current = _uiState.value.selectedSubjectIds
                val newSelection = if (event.subjectId in current) current - event.subjectId else current + event.subjectId
                _uiState.update { it.copy(selectedSubjectIds = newSelection) }
            }
            is SubjectEvent.ClearSelection -> {
                _uiState.update { it.copy(isSelectionMode = false, selectedSubjectIds = emptySet()) }
            }
            is SubjectEvent.DeleteSelectedSubjects -> {
                viewModelScope.launch {
                    val selectedIds = _uiState.value.selectedSubjectIds
                    allSubjects.filter { it.id in selectedIds }.forEach { subject ->
                        repository.deleteSubject(subject)
                    }
                    _uiState.update { it.copy(isSelectionMode = false, selectedSubjectIds = emptySet()) }
                }
            }
            is SubjectEvent.SelectAllSubjects -> {
                val allIds = _uiState.value.subjects.map { it.id }.toSet()
                _uiState.update { it.copy(selectedSubjectIds = allIds) }
            }
            is SubjectEvent.DeselectAllSubjects -> {
                _uiState.update {
                    it.copy(
                        selectedSubjectIds = emptySet()
                    )
                }
            }
            is SubjectEvent.RestoreSubject -> {
               viewModelScope.launch { repository.insertSubject(event.subject) }
            }
        }
    }
}
