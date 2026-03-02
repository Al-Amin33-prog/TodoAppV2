package com.example.todoappv2.subject.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubjectAddEditViewModel (
    private val repository: AppRepository,
    private val subjectId: Long? = null
): ViewModel(){
    private val _subjectAddEditEvent = MutableSharedFlow<SubjectAddEditEvent>()
    val subjectAddEditEvent = _subjectAddEditEvent.asSharedFlow()
    private val _uiState = MutableStateFlow(SubjectAddEditUiState())
    val uiState = _uiState.asStateFlow()
    init {
        subjectId.let{
            loadSubject(it)
        }

    }
    private fun loadSubject(id: Long?){
        viewModelScope.launch {
            val subject = repository.getSubjectById(id)
            subject?.let {
                _uiState.value = _uiState.value.copy(
                   title = it.name,
                    colorHex = it.colorHex,
                    createdAt = it.createdAt,
                    isEditMode = true
                )
            }
        }

    }
    fun onTitleChange(newTitle: String){
        _uiState.value = _uiState.value.copy(title = newTitle)
    }
    fun onColorChanged(newColorHex: Long){
        _uiState.value = _uiState.value.copy(
            colorHex = newColorHex
        )
    }
    fun onSave(){
        val state = _uiState.value
        if (state.title.isBlank()){
            _uiState.value = state.copy(error = "Tittle cannot be empty")
            return
        }
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            if (state.isEditMode && subjectId != null){
             repository.updateSubject(
                 SubjectEntity(
                     id = subjectId,
                     name    = state.title,
                     colorHex = state.colorHex,
                     createdAt = state.createdAt

                 )
             )



        }
            else{
                repository.insertSubject(
                    SubjectEntity(
                        name = state.title                        ,
                        colorHex = state.colorHex,
                        createdAt = System.currentTimeMillis()
                    )
                )
            }
            _uiState.value = state.copy(isSaving =  false)
            _subjectAddEditEvent.emit(SubjectAddEditEvent.Success)
    }

}
}