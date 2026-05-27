package com.example.todoappv2.ml

import android.content.Context
import com.example.todoappv2.data.local.entity.TaskEntity
import kotlin.math.sqrt

/**
 * TaskPriorityModel - ML model for predicting task priority
 * Uses statistical analysis on historical task data to predict priority levels
 * 
 * Algorithm: Naive Bayes classifier with custom feature engineering
 * Features: title length, subject pattern, time to deadline, task history
 */
class TaskPriorityModel(private val context: Context) {

    /**
     * Priority levels: Low (0), Medium (1), High (2), Urgent (3)
     */
    enum class PriorityLevel(val value: Int, val label: String) {
        LOW(0, "Low"),
        MEDIUM(1, "Medium"),
        HIGH(2, "High"),
        URGENT(3, "Urgent")
    }

    /**
     * Feature set for ML model
     */
    data class TaskFeatures(
        val titleLength: Int,
        val hasKeywords: Boolean,
        val daysToDeadline: Int,
        val subjectCompletionRate: Float,
        val isOverdue: Boolean
    )

    /**
     * Training data - stores historical patterns
     * Format: Priority -> List of feature patterns
     */
    private val trainingData = mutableMapOf<Int, MutableList<TaskFeatures>>()

    init {
        // Initialize empty training data for each priority level
        for (i in 0..3) {
            trainingData[i] = mutableListOf()
        }
    }

    /**
     * Extracts features from a task
     */
    fun extractFeatures(
        task: TaskEntity,
        subjectCompletionRate: Float = 0.5f,
        allTasksCount: Int = 0
    ): TaskFeatures {
        val titleLength = task.title.length
        val hasKeywords = detectUrgencyKeywords(task.title)
        
        val daysToDeadline = if (task.dueDate != null) {
            ((task.dueDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
        } else {
            365 // Default: far away
        }
        
        val isOverdue = task.dueDate != null && task.dueDate < System.currentTimeMillis()

        return TaskFeatures(
            titleLength = titleLength,
            hasKeywords = hasKeywords,
            daysToDeadline = daysToDeadline,
            subjectCompletionRate = subjectCompletionRate,
            isOverdue = isOverdue
        )
    }

    /**
     * Detects urgency keywords in task title
     * Keywords: ASAP, urgent, critical, important, deadline, etc.
     */
    private fun detectUrgencyKeywords(title: String): Boolean {
        val urgencyKeywords = listOf(
            "asap", "urgent", "critical", "important",
            "deadline", "today", "tonight", "immediately",
            "must", "essential", "priority", "rush"
        )
        return urgencyKeywords.any { title.lowercase().contains(it) }
    }

    /**
     * Train the model with labeled task data
     * Call this when user marks tasks with priorities
     */
    fun train(task: TaskEntity, actualPriority: Int, subjectCompletionRate: Float = 0.5f) {
        val features = extractFeatures(task, subjectCompletionRate)
        trainingData[actualPriority]?.add(features)
    }

    /**
     * Predict priority for a new task
     * Returns PriorityLevel (Low, Medium, High, Urgent)
     */
    fun predictPriority(
        task: TaskEntity,
        subjectCompletionRate: Float = 0.5f
    ): PriorityLevel {
        if (trainingData.values.all { it.isEmpty() }) {
            // No training data - return default Medium
            return PriorityLevel.MEDIUM
        }

        val features = extractFeatures(task, subjectCompletionRate)
        var bestPriority = 1 // Default: Medium
        var bestScore = Double.MIN_VALUE

        // Calculate probability for each priority level
        for (priority in 0..3) {
            val score = calculateProbability(features, priority)
            if (score > bestScore) {
                bestScore = score
                bestPriority = priority
            }
        }

        return PriorityLevel.values().find { it.value == bestPriority } ?: PriorityLevel.MEDIUM
    }

    /**
     * Calculate probability using Naive Bayes approach
     * P(Priority | Features) ∝ P(Features | Priority) * P(Priority)
     */
    private fun calculateProbability(features: TaskFeatures, priorityLevel: Int): Double {
        val trainingExamples = trainingData[priorityLevel] ?: return 0.0
        
        if (trainingExamples.isEmpty()) return 0.0

        // Prior probability: P(Priority)
        val totalSamples = trainingData.values.sumOf { it.size }
        val priorProbability = trainingExamples.size.toDouble() / totalSamples

        // Likelihood: P(Features | Priority)
        var likelihood = 1.0
        
        // Feature 1: Title length similarity
        val avgTitleLength = trainingExamples.map { it.titleLength }.average()
        val titleSimilarity = 1.0 / (1.0 + kotlin.math.abs(features.titleLength - avgTitleLength) / 10.0)
        likelihood *= titleSimilarity

        // Feature 2: Keyword match
        val keywordMatch = if (features.hasKeywords) {
            trainingExamples.count { it.hasKeywords }.toDouble() / trainingExamples.size
        } else {
            1.0 - (trainingExamples.count { it.hasKeywords }.toDouble() / trainingExamples.size)
        }
        likelihood *= keywordMatch

        // Feature 3: Days to deadline
        val avgDaysToDeadline = trainingExamples.map { it.daysToDeadline }.average()
        val deadlineSimilarity = 1.0 / (1.0 + kotlin.math.abs(features.daysToDeadline - avgDaysToDeadline) / 7.0)
        likelihood *= deadlineSimilarity

        // Feature 4: Subject completion rate
        val avgCompletionRate = trainingExamples.map { it.subjectCompletionRate }.average()
        val completionSimilarity = 1.0 - kotlin.math.abs(features.subjectCompletionRate - avgCompletionRate)
        likelihood *= completionSimilarity

        // Feature 5: Overdue penalty
        if (features.isOverdue) {
            likelihood *= 1.5 // Boost score for overdue tasks
        }

        // Combine prior and likelihood
        return priorProbability * likelihood
    }

    /**
     * Get prediction confidence (0.0 - 1.0)
     * Higher value = model is more confident
     */
    fun getPredictionConfidence(
        task: TaskEntity,
        subjectCompletionRate: Float = 0.5f
    ): Float {
        if (trainingData.values.all { it.isEmpty() }) return 0.3f // Low confidence without training
        
        val features = extractFeatures(task, subjectCompletionRate)
        val scores = (0..3).map { calculateProbability(features, it) }
        val maxScore = scores.maxOrNull() ?: 0.0
        val sumScores = scores.sum()
        
        return if (sumScores > 0) (maxScore / sumScores).toFloat() else 0.3f
    }

    /**
     * Get model statistics
     */
    fun getModelStats(): Map<String, Any> {
        return mapOf(
            "totalTrainingSamples" to trainingData.values.sumOf { it.size },
            "samplesPerPriority" to trainingData.mapKeys { (k, _) -> 
                PriorityLevel.values().find { it.value == k }?.label ?: "Unknown"
            }.mapValues { it.value.size },
            "isModelTrained" to (trainingData.values.any { it.isNotEmpty() })
        )
    }
}
