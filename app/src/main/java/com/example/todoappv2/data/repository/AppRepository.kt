package com.example.todoappv2.data.repository

import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun insertSubject(subject: SubjectEntity)
    suspend fun updateSubject(subject: SubjectEntity)
    suspend fun deleteSubject(subject: SubjectEntity)
    suspend fun getSubjectById(subjectId: Long?): SubjectEntity?
    fun getSubjects(): Flow<List<SubjectEntity>>

    suspend fun insertTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun getTaskById(taskId: Long): TaskEntity?
    fun getTasKBySubject(subjectId: Long): Flow<List<TaskEntity>>
}
