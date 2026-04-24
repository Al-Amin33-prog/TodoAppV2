package com.example.todoappv2.domain.usecases

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.TaskUiModel

fun TaskUiModel.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        subjectId = subjectId,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}