package com.example.todoappv2.domain.usecases

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.TaskUiModel
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class UiModelUseCase @Inject constructor() {
    operator fun invoke(task: TaskEntity): TaskUiModel{
        val now = System.currentTimeMillis()
        val dueLabel = when {

            task.isCompleted ->
                "Completed"

            task.dueDate == null ->
                null

            task.dueDate < now ->
                "Overdue"

            isToday(task.dueDate) ->
                "Today"

            else ->
                "Upcoming"
        }
        return TaskUiModel(
            id = task.id,
            title = task.title,
            description = task.description,
            isOverDue =!task.isCompleted && task.dueDate?.let { it < now } == true,
            isCompleted = task.isCompleted,
            dueLabel =dueLabel,
            dueDate = task.dueDate,
            subjectId = task.subjectId,
            createdAt = task.createdAt,
            predictedPriority = "Medium",
            priorityConfidence = 0.5f
        )
    }
}

private fun isToday(time:Long):
        Boolean{
    val todayStart = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    val tomorrowStart = LocalDate.now()
        .plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    return time in todayStart until tomorrowStart
}