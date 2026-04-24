package com.example.todoappv2.domain.usecases

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.task.TaskUiModel
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class UiModelUseCase @Inject constructor() {
    operator fun invoke(task: TaskEntity): TaskUiModel{
        val now = System.currentTimeMillis()
        val dueLabel = task.dueDate?.let{
            when{
               it< now && ! task.isCompleted -> "Overdue"
               isToday(it) -> "Today"
                else -> "Upcoming"
            }

        }
        return TaskUiModel(
            id = task.id,
            title = task.title,
            description = task.description,
            isOverDue = task.dueDate?.let {
                it < now
            } == true,
            isCompleted = task.isCompleted,
            dueLabel =dueLabel,
            dueDate = task.dueDate,
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