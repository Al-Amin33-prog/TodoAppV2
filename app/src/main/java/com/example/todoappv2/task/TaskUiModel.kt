package com.example.todoappv2.task



class TaskUiModel (

    val id: Long,
    val subjectId: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val dueDate: Long?,
    val dueLabel: String?,
    val isOverDue: Boolean,
    val createdAt: Long,
    val priority: String,
    val predictedPriority: String = "Medium",
    val priorityConfidence: Float = 0.5f
)
fun TaskUiModel.copy(
    id: Long = this.id,
    predictedPriority: String = this.predictedPriority,
    priorityConfidence: Float = this.priorityConfidence
) = TaskUiModel(
    id = id,
    predictedPriority = predictedPriority,
    priorityConfidence = priorityConfidence,
    subjectId = subjectId,
    title = title,
    description = description,
    isCompleted = isCompleted,
    dueDate = dueDate,
    dueLabel = dueLabel,
    isOverDue = isOverDue,
    createdAt = createdAt,
    priority = priority
)
