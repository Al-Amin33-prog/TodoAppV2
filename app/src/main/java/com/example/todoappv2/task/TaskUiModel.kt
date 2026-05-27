package com.example.todoappv2.task

data class TaskUiModel(
    val id: Long,
    val subjectId: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val dueDate: Long?,
    val dueLabel: String?,
    val isOverDue: Boolean,
    val createdAt: Long,
    
    // 🆕 ML FIELDS FOR PRIORITY PREDICTION
    val predictedPriority: String = "Medium",  // Low, Medium, High, Urgent
    val priorityConfidence: Float = 0.5f      // 0.0 - 1.0 confidence score
)

/**
 * Extension function to create a copy of TaskUiModel with updated fields
 * Allows partial updates without rewriting all parameters
 */
fun TaskUiModel.copy(
    id: Long = this.id,
    subjectId: Long = this.subjectId,
    title: String = this.title,
    description: String? = this.description,
    isCompleted: Boolean = this.isCompleted,
    dueDate: Long? = this.dueDate,
    dueLabel: String? = this.dueLabel,
    isOverDue: Boolean = this.isOverDue,
    createdAt: Long = this.createdAt,
    predictedPriority: String = this.predictedPriority,
    priorityConfidence: Float = this.priorityConfidence
) = TaskUiModel(
    id = id,
    subjectId = subjectId,
    title = title,
    description = description,
    isCompleted = isCompleted,
    dueDate = dueDate,
    dueLabel = dueLabel,
    isOverDue = isOverDue,
    createdAt = createdAt,
    predictedPriority = predictedPriority,
    priorityConfidence = priorityConfidence
)
