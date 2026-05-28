package com.example.todoappv2.ml

import android.content.Context
import com.example.todoappv2.data.local.entity.TaskEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MLHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val priorityModel: TaskPriorityModel = TaskPriorityModel(context)
    private val preprocessor = MLDataPreprocessor()


    fun trainModelWithHistoricalData(
        allTasks: List<TaskEntity>,
        taskPriorities: Map<Long, Int> // taskId -> priority level
    ) {
        val validTasks = preprocessor.prepareTrainingData(allTasks)

        validTasks.forEach { task ->
            val priority = taskPriorities[task.id] ?: 1 // Default to Medium
            // For now, we train only tasks that are not completed
            // In production, you'd train on all historical data
            if (task.isCompleted || System.currentTimeMillis() - task.createdAt > 30L * 24 * 60 * 60 * 1000) {
                priorityModel.train(task, priority)
            }
        }
    }


    fun predictTaskPriority(
        task: TaskEntity,
        allTasks: List<TaskEntity>
    ): TaskPriorityModel.PriorityLevel {
        val completionRate = preprocessor.calculateSubjectCompletionRate(task.subjectId, allTasks)
        return priorityModel.predictPriority(task, completionRate)
    }


    fun getPredictionConfidence(
        task: TaskEntity,
        allTasks: List<TaskEntity>
    ): Float {
        val completionRate = preprocessor.calculateSubjectCompletionRate(task.subjectId, allTasks)
        return priorityModel.getPredictionConfidence(task, completionRate)
    }




    /**
     * Update model with user feedback
     * Call this when user manually sets a priority (feedback loop)
     */
    fun feedbackTask(
        task: TaskEntity,
        actualPriority: Int,
        allTasks: List<TaskEntity>
    ) {
        val completionRate = preprocessor.calculateSubjectCompletionRate(task.subjectId, allTasks)
        priorityModel.train(task, actualPriority, completionRate)
    }


}
