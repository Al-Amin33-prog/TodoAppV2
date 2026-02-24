package com.example.todoappv2.task.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoappv2.data.repository.AppRepository

class TaskAddEditViewModelFactory(
    private val repository: AppRepository,
    private val taskId: Long? = null,
    private val subjectId: Long? = null
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskAddEditViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TaskAddEditViewModel(repository,taskId,subjectId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}