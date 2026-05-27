package com.example.todoappv2.ml

import android.content.Context
import com.example.todoappv2.data.local.entity.TaskEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MLHelper - Singleton utility for ML operations
 * Manages model training, prediction, and caching
 */
@Singleton
class MLHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val priorityModel: TaskPriorityModel = TaskPriorityModel(context)
    private val preprocessor = MLDataPreprocessor()
    
    /**
     * Train model with historical task data
     * Should be called once when app starts or periodically
     */
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

    /**
     * Predict priority for a single task
     */
    fun predictTaskPriority(
        task: TaskEntity,
        allTasks: List<TaskEntity>
    ): TaskPriorityModel.PriorityLevel {
        val completionRate = preprocessor.calculateSubjectCompletionRate(task.subjectId, allTasks)
        return priorityModel.predictPriority(task, completionRate)
    }

    /**
     * Get prediction confidence
     */
    fun getPredictionConfidence(
        task: TaskEntity,
        allTasks: List<TaskEntity>
    ): Float {
        val completionRate = preprocessor.calculateSubjectCompletionRate(task.subjectId, allTasks)
        return priorityModel.getPredictionConfidence(task, completionRate)
    }

    /**
     * Predict priorities for multiple tasks
     * Returns: taskId -> predicted priority
     */
    fun predictMultipleTaskPriorities(
        tasks: List<TaskEntity>,
        allTasks: List<TaskEntity>
    ): Map<Long, TaskPriorityModel.PriorityLevel> {
        return tasks.associate { task ->
            task.id to predictTaskPriority(task, allTasks)
        }
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

    /**
     * Get ML model insights
     */
    fun getModelInsights(): Map<String, Any> {
        return priorityModel.getModelStats()
    }

    /**
     * Get productivity analytics
     */
    fun getProductivityAnalytics(allTasks: List<TaskEntity>): Map<String, Any> {
        val overdueCount = preprocessor.getOverdueTaskCount(allTasks)
        val upcomingTasks = preprocessor.getUpcomingTasks(allTasks)
        val productivityRate = preprocessor.calculateOverallProductivityRate(allTasks)
        
        return mapOf(
            "totalTasks" to allTasks.size,
            "completedTasks" to allTasks.count { it.isCompleted },
            "pendingTasks" to allTasks.count { !it.isCompleted },
            "overdueTasks" to overdueCount,
            "upcomingTasksNextWeek" to upcomingTasks.size,
            "productivityRate" to productivityRate,
            "modelTrainingSamples" to priorityModel.getModelStats()["totalTrainingSamples"]
        )
    }

    /**
     * Check if model is properly trained
     */
    fun isModelTrained(): Boolean {
        val stats = priorityModel.getModelStats()
        return (stats["totalTrainingSamples"] as? Int ?: 0) > 5
    }
}
