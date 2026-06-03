package com.example.todoappv2.ml

import android.content.Context

import com.example.todoappv2.data.local.entity.TaskEntity
import com.example.todoappv2.data.repository.MlRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// MLHelper.kt - FIXED
@Singleton
class MLHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mlRepository: MlRepository,
) {
    private val priorityModel: TaskPriorityModel = TaskPriorityModel(context)
    private val preprocessor = MLDataPreprocessor()
    private var isModelInitialized = false




    // ✅ NEW: Initialize model with historical data on first use
    suspend fun initializeModel() {

        if (isModelInitialized) return

        withContext(Dispatchers.Default) {

            val savedSamples =
                mlRepository.getTrainingSamples()

            savedSamples.forEach { sample ->

                val task = TaskEntity(
                    id = sample.id,
                    subjectId = sample.subjectId,
                    title = sample.taskTitle,
                    dueDate = sample.dueDate,
                    isCompleted = sample.isCompleted,
                    createdAt = sample.createdAt
                )

                priorityModel.train(
                    task,
                    sample.priorityLevel
                )
            }

            isModelInitialized = true
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

    suspend fun feedbackTask(
        task: TaskEntity,
        actualPriority: Int,
        allTasks: List<TaskEntity>
    ) {

        val completionRate =
            preprocessor.calculateSubjectCompletionRate(
                task.subjectId,
                allTasks
            )

        priorityModel.train(
            task,
            actualPriority,
            completionRate
        )

        mlRepository.saveTrainingSample(
            task,
            actualPriority
        )
    }


}
