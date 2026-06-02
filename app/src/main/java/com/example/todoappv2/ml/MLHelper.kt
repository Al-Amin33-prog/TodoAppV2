package com.example.todoappv2.ml

import android.content.Context
import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.AppRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

// MLHelper.kt - FIXED
@Singleton
class MLHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AppRepository  // ✅ NEW: To load historical data
) {
    private val priorityModel: TaskPriorityModel = TaskPriorityModel(context)
    private val preprocessor = MLDataPreprocessor()
    private var isModelInitialized = false


    // ✅ NEW: Initialize model with historical data on first use
    suspend fun initializeModel(allTasks: List<TaskEntity>) {
        if (isModelInitialized) return

        try {
            val previousTasks = repository.getAllTasks().first()

            trainModelWithHistoricalData(
                previousTasks,
                previousTasks.associate { it.id to getPriorityValue(it) }
            )
        } catch (e: Exception) {
            // If loading fails, train with current tasks only
            trainModelWithHistoricalData(
                allTasks,
                allTasks.associate { it.id to getPriorityValue(it) }
            )
        }

        isModelInitialized = true
    }

    private fun getPriorityValue(task: TaskEntity): Int {
        return when (task.priority) {
            "Low" -> 0
            "Medium" -> 1
            "High" -> 2
            "Urgent" -> 3
            else -> 1
        }
    }

    fun trainModelWithHistoricalData(
        allTasks: List<TaskEntity>,
        taskPriorities: Map<Long, Int>
    ) {
        val validTasks = preprocessor.prepareTrainingData(allTasks)

        validTasks.forEach { task ->
            val priority = taskPriorities[task.id] ?: 1
            // ✅ FIXED: Train on RECENT, incomplete tasks (inverted logic)
            if (!task.isCompleted && System.currentTimeMillis() - task.createdAt < 30L * 24 * 60 * 60 * 1000) {
                priorityModel.train(task, priority)
            }
        }
    }
    private fun getDefaultPriority(task: TaskEntity): Int {
        // If task has no priority field, use the predicted priority
        // For now, default to Medium (1)
        return 1
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

    fun feedbackTask(
        task: TaskEntity,
        actualPriority: Int,
        allTasks: List<TaskEntity>
    ) {
        val completionRate = preprocessor.calculateSubjectCompletionRate(task.subjectId, allTasks)
        priorityModel.train(task, actualPriority, completionRate)
    }

    fun getModelStats(): Map<String, Any> {
        return priorityModel.getModelStats()
    }
}
