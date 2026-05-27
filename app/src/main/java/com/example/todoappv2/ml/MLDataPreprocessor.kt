package com.example.todoappv2.ml

import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity

/**
 * MLDataPreprocessor - Handles data preparation for ML model
 * Calculates statistics and prepares training data
 */
class MLDataPreprocessor {

    /**
     * Calculate subject completion rate
     * Returns: (completed tasks) / (total tasks) for a subject
     */
    fun calculateSubjectCompletionRate(
        subjectId: Long,
        allTasks: List<TaskEntity>
    ): Float {
        val subjectTasks = allTasks.filter { it.subjectId == subjectId }
        if (subjectTasks.isEmpty()) return 0.5f // Default neutral rate
        
        val completedCount = subjectTasks.count { it.isCompleted }
        return completedCount.toFloat() / subjectTasks.size
    }

    /**
     * Calculate overall user productivity rate
     */
    fun calculateOverallProductivityRate(allTasks: List<TaskEntity>): Float {
        if (allTasks.isEmpty()) return 0.5f
        val completedCount = allTasks.count { it.isCompleted }
        return completedCount.toFloat() / allTasks.size
    }

    /**
     * Get overdue task count
     */
    fun getOverdueTaskCount(allTasks: List<TaskEntity>): Int {
        val now = System.currentTimeMillis()
        return allTasks.count { task ->
            task.dueDate != null && task.dueDate < now && !task.isCompleted
        }
    }

    /**
     * Get upcoming tasks (due in next 7 days)
     */
    fun getUpcomingTasks(allTasks: List<TaskEntity>, daysAhead: Int = 7): List<TaskEntity> {
        val now = System.currentTimeMillis()
        val sevenDaysLater = now + (daysAhead * 24 * 60 * 60 * 1000)
        
        return allTasks.filter { task ->
            task.dueDate != null && 
            task.dueDate in (now + 1)..sevenDaysLater && 
            !task.isCompleted
        }
    }

    /**
     * Normalize feature values to 0-1 range
     */
    fun normalizeValue(value: Float, min: Float, max: Float): Float {
        if (max <= min) return 0.5f
        return (value - min) / (max - min)
    }

    /**
     * Get task difficulty score based on description length
     * Longer description = potentially more complex task
     */
    fun getTaskDifficultyScore(task: TaskEntity): Float {
        val descLength = task.description?.length ?: 0
        return minOf(descLength.toFloat() / 200, 1.0f) // Normalize to 0-1
    }

    /**
     * Detect task category based on title and subject
     */
    fun detectTaskCategory(task: TaskEntity, subjects: List<SubjectEntity>): String {
        val subject = subjects.find { it.id == task.subjectId }
        return subject?.name ?: "General"
    }

    /**
     * Calculate time until deadline in hours
     */
    fun getTimeUntilDeadlineHours(task: TaskEntity): Int {
        if (task.dueDate == null) return Int.MAX_VALUE
        val hoursRemaining = (task.dueDate - System.currentTimeMillis()) / (1000 * 60 * 60)
        return hoursRemaining.toInt()
    }

    /**
     * Get task age in days (days since creation)
     */
    fun getTaskAgeDays(task: TaskEntity): Int {
        val ageMs = System.currentTimeMillis() - task.createdAt
        return (ageMs / (1000 * 60 * 60 * 24)).toInt()
    }

    /**
     * Prepare all tasks for model training
     * Filters out incomplete data and prepares features
     */
    fun prepareTrainingData(allTasks: List<TaskEntity>): List<TaskEntity> {
        return allTasks.filter { task ->
            task.title.isNotBlank() && 
            task.subjectId > 0
        }
    }

    /**
     * Check if task has critical attributes
     * Used to filter low-quality training samples
     */
    fun isCriticalTaskValid(task: TaskEntity): Boolean {
        return task.title.isNotBlank() &&
                task.subjectId > 0 &&
                task.dueDate != null &&
                task.createdAt > 0
    }
}
