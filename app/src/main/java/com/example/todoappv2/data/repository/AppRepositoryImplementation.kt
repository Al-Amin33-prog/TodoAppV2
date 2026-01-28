package com.example.todoappv2.data.repository

import com.example.todoappv2.data.local.dao.SubjectDao
import com.example.todoappv2.data.local.dao.TaskDao
import com.example.todoappv2.data.local.entity.SubjectEntity
import com.example.todoappv2.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class AppRepositoryImplementation (
    private val subjectDao: SubjectDao,
    private val taskDao: TaskDao
): AppRepository{
    override suspend fun insertSubject(subject: SubjectEntity) {
       subjectDao.insertSubject(subject)
    }

    override suspend fun updateSubject(subject: SubjectEntity) {
        subjectDao.updateSubject(subject)
    }

    override suspend fun deleteSubject(subject: SubjectEntity) {
        subjectDao.deleteSubject(subject)
    }

    override fun getSubjects(): Flow<List<SubjectEntity>> {
        return subjectDao.getAllSubjects()
    }

    override suspend fun getSubjectById(subjectId: Long?): SubjectEntity {
        return subjectDao.getSubjectById(subjectId)
    }

    override suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    override suspend fun getTaskById(taskId: Long): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }

    override fun getTasKBySubject(subjectId: Long): Flow<List<TaskEntity>> {
        return taskDao.getTasksBySubject(subjectId)
    }

}