package com.example.todoappv2.domain.usecases


import com.example.todoappv2.task.TaskUiModel
import com.example.todoappv2.task.components.TaskSection
import javax.inject.Inject

class GroupTaskUseCases  @Inject
constructor(){
    operator fun invoke(tasks: List<TaskUiModel>): Map<TaskSection,
            List<TaskUiModel>>{
        val now = System.currentTimeMillis()
        return tasks.groupBy { task ->
            when{
                task.dueDate == null ->
                    TaskSection.NoDate
                task.dueDate < now && !task.isCompleted ->
                    TaskSection.Overdue
                isToday(task.dueDate) ->
                    TaskSection.Today
                else ->
                    TaskSection.Upcoming
            }
        }
    }
    private fun isToday(time:Long):
            Boolean{
        val todayStart = java.time.LocalDate.now()
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val tomorrowStart = java.time.LocalDate.now()
            .plusDays(1)
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        return time in todayStart until tomorrowStart
    }
}
