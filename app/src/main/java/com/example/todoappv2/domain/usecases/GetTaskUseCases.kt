package com.example.todoappv2.domain.usecases

import com.example.todoappv2.data.repository.AppRepository
import javax.inject.Inject

class GetTaskUseCases  @Inject
constructor(
    private val repository: AppRepository
){
    operator fun invoke() = repository.getAllTasks()
}