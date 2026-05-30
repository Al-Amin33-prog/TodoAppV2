package com.example.todoappv2.ml


import com.example.todoappv2.data.local.entity.TaskEntity

/**
 * MLDataPreprocessor - Handles data preparation for ML model
 * Calculates statistics and prepares training data
 */
class MLDataPreprocessor {


    fun calculateSubjectCompletionRate(
        subjectId: Long,
        allTasks: List<TaskEntity>
    ): Float {
        val subjectTasks = allTasks.filter { it.subjectId == subjectId }
        if (subjectTasks.isEmpty()) return 0.5f // Default neutral rate
        
        val completedCount = subjectTasks.count { it.isCompleted }
        return completedCount.toFloat() / subjectTasks.size
    }


    fun prepareTrainingData(allTasks: List<TaskEntity>): List<TaskEntity> {
        return allTasks.filter { task ->
            task.title.isNotBlank() && 
            task.subjectId > 0
        }
    }



}
