package com.example.todoappv2.ml

import android.content.Context
import com.example.todoappv2.data.local.entity.TaskEntity
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

/**
 * TaskPriorityModel - Optimized ML model for predicting task priority.
 * 
 * FIXES:
 * 1. Laplace Smoothing: Prevents 0% probability when a feature hasn't been seen yet.
 * 2. Log-Probabilities: Prevents numerical underflow and improves accuracy.
 * 3. Feature Weighting: Urgency keywords are now given significantly higher weight (5x).
 * 4. Confidence Fix: Removed the hardcoded 0.85 multiplier.
 */
class TaskPriorityModel(private val context: Context) {

    enum class PriorityLevel(val value: Int, val label: String) {
        LOW(0, "Low"),
        MEDIUM(1, "Medium"),
        HIGH(2, "High"),
        URGENT(3, "Urgent")
    }

    data class TaskFeatures(
        val titleLength: Int,
        val hasHighKeywords: Boolean,
        val hasLowKeywords: Boolean,
        val daysToDeadline: Int,
        val subjectCompletionRate: Float,
        val isOverdue: Boolean
    )

    private val trainingData = mutableMapOf<Int, MutableList<TaskFeatures>>()

    init {
        for (i in 0..3) {
            trainingData[i] = mutableListOf()
        }
    }

    fun extractFeatures(
        task: TaskEntity,
        subjectCompletionRate: Float = 0.5f
    ): TaskFeatures {
        val title = task.title.lowercase()
        val days = if (task.dueDate != null) {
            ((task.dueDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
        } else 30 

        return TaskFeatures(
            titleLength = title.length,
            hasHighKeywords = detectHighKeywords(title),
            hasLowKeywords = detectLowKeywords(title),
            daysToDeadline = days,
            subjectCompletionRate = subjectCompletionRate,
            isOverdue = task.dueDate != null && task.dueDate < System.currentTimeMillis()
        )
    }

    private fun detectHighKeywords(title: String): Boolean {
        val urgencyKeywords = listOf(
            "asap", "urgent", "critical", "important", "deadline", "immediately",
            "must", "priority", "rush", "now", "today", "tonight"
        )
        return urgencyKeywords.any { title.contains(it) }
    }

    private fun detectLowKeywords(title: String): Boolean {
        val lowKeywords = listOf("whenever", "maybe", "optional", "low", "backlog", "relax", "later")
        return lowKeywords.any { title.contains(it) }
    }

    fun train(task: TaskEntity, actualPriority: Int, subjectCompletionRate: Float = 0.5f) {
        val features = extractFeatures(task, subjectCompletionRate)
        trainingData[actualPriority]?.add(features)
    }

    fun predictPriority(
        task: TaskEntity,
        subjectCompletionRate: Float = 0.5f
    ): PriorityLevel {
        val features = extractFeatures(task, subjectCompletionRate)

        // Heuristic fallback if no training data exists
        if (trainingData.values.all { it.isEmpty() }) {
            return when {
                features.isOverdue -> PriorityLevel.URGENT
                features.hasHighKeywords -> PriorityLevel.HIGH
                features.daysToDeadline <= 1 -> PriorityLevel.HIGH
                features.hasLowKeywords -> PriorityLevel.LOW
                else -> PriorityLevel.MEDIUM
            }
        }

        var bestPriority = 1 
        var maxLogProb = Double.NEGATIVE_INFINITY

        for (priority in 0..3) {
            val logProb = calculateLogProbability(features, priority)
            if (logProb > maxLogProb) {
                maxLogProb = logProb
                bestPriority = priority
            }
        }

        return PriorityLevel.values().find { it.value == bestPriority } ?: PriorityLevel.MEDIUM
    }

    private fun calculateLogProbability(features: TaskFeatures, priorityLevel: Int): Double {
        val examples = trainingData[priorityLevel] ?: return -100.0
        val totalSamples = trainingData.values.sumOf { it.size }.toDouble()

        // 1. Class Prior with Laplace Smoothing (+1)
        val prior = (examples.size + 1).toDouble() / (totalSamples + 4)
        var logLikelihood = log10(prior)

        // 2. Keyword Likelihood (Weighted 5x)
        val highKCount = examples.count { it.hasHighKeywords }.toDouble()
        val highKLikelihood = (highKCount + 1) / (examples.size + 2)
        val keywordTerm = if (features.hasHighKeywords) log10(highKLikelihood) else log10(1.0 - highKLikelihood)
        logLikelihood += (keywordTerm * 5.0) 

        // 3. Deadline Proximity
        val avgDays = if (examples.isNotEmpty()) examples.map { it.daysToDeadline }.average() else 7.0
        val deadlineLikelihood = 1.0 / (1.0 + abs(features.daysToDeadline - avgDays) / 7.0)
        logLikelihood += log10(deadlineLikelihood)

        // 4. Overdue logic
        if (features.isOverdue) {
            logLikelihood += if (priorityLevel >= 2) 0.5 else -0.5
        }

        return logLikelihood
    }

    fun getPredictionConfidence(
        task: TaskEntity,
        subjectCompletionRate: Float = 0.5f
    ): Float {
        if (trainingData.values.all { it.isEmpty() }) return 0.45f

        val features = extractFeatures(task, subjectCompletionRate)
        val logProbs = (0..3).map { calculateLogProbability(features, it) }

        val maxLog = logProbs.maxOrNull() ?: 0.0
        val sumExp = logProbs.sumOf { 10.0.pow(it - maxLog) }
        val relativeProb = 1.0 / sumExp

        return relativeProb.toFloat().coerceIn(0.35f, 0.99f)
    }

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
