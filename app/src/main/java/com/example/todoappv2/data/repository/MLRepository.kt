package com.example.todoappv2.data.repository

import com.example.todoappv2.data.local.dao.PriorityTrainingDao
import com.example.todoappv2.data.local.entity.PriorityTrainingEntity
import com.example.todoappv2.data.local.entity.TaskEntity
import javax.inject.Inject


class MlRepository @Inject constructor(
    private val trainingDao: PriorityTrainingDao
) {

    suspend fun saveTrainingSample(
        task: TaskEntity,
        priority: Int
    ) {

        trainingDao.insertTrainingSample(
            PriorityTrainingEntity(
                taskTitle = task.title,
                subjectId = task.subjectId,
                dueDate = task.dueDate,
                isCompleted = task.isCompleted,
                createdAt = task.createdAt,
                priorityLevel = priority
            )
        )
        trainingDao.trimDataset()
    }

    suspend fun getTrainingSamples(): List<PriorityTrainingEntity> {
        return trainingDao.getAllTrainingSamples()
    }

}