package com.example.todoappv2.domain.usecases

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.components.TaskFilterType
import javax.inject.Inject

class FilterTaskUseCases @Inject
constructor(){
    operator fun invoke(
        tasks: List<TaskEntity>,
        filter: TaskFilterType
    ):List<TaskEntity>{
        return when(filter){
            TaskFilterType.All -> tasks
            TaskFilterType.Completed -> tasks.filter { it.isCompleted }
            TaskFilterType.Pending -> tasks.filter { !it.isCompleted }
            TaskFilterType.Overdue ->
                tasks.filter {
                    it.dueDate != null &&
                            it.dueDate < System.currentTimeMillis() &&
                            !it.isCompleted
                }
            is
            TaskFilterType.BySubject -> tasks.filter {
                it.subjectId == filter.subjectId
            }

        }
    }
}